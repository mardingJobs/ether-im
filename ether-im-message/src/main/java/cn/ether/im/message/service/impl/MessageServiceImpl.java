package cn.ether.im.message.service.impl;


import cn.ether.im.common.enums.ChatMessageType;
import cn.ether.im.common.enums.ImMessageEventType;
import cn.ether.im.common.enums.ImMessageStatus;
import cn.ether.im.common.enums.ImTerminalType;
import cn.ether.im.common.exception.ImException;
import cn.ether.im.common.model.ImChatMessageSentResult;
import cn.ether.im.common.model.message.ImChatMessage;
import cn.ether.im.common.model.message.ImGroupMessage;
import cn.ether.im.common.model.message.ImMessageEvent;
import cn.ether.im.common.model.message.ImPersonalMessage;
import cn.ether.im.common.model.user.ImUser;
import cn.ether.im.common.model.user.ImUserTerminal;
import cn.ether.im.common.util.SnowflakeUtil;
import cn.ether.im.message.dto.GroupChatMessageReq;
import cn.ether.im.message.dto.PersonalChatMessageReq;
import cn.ether.im.message.entity.ImMessageEventLogEntity;
import cn.ether.im.message.entity.ImPersonalMessageEntity;
import cn.ether.im.message.service.ImMessageEventLogEntityService;
import cn.ether.im.message.service.ImPersonalMessageService;
import cn.ether.im.message.service.MessageService;
import cn.ether.im.sdk.client.EtherImClient;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * * @Author: Martin
 * * @Date    2024/9/15 14:58
 * * @Description
 **/
@Slf4j
@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private EtherImClient etherImClient;

    @Resource
    private SnowflakeUtil snowflakeUtil;

    @Resource
    private ImPersonalMessageService personalMessageService;

    @Resource
    private ImMessageEventLogEntityService messageEventLogEntityService;


    private ImPersonalMessageEntity toEntity(PersonalChatMessageReq req) {
        ImPersonalMessageEntity entity = new ImPersonalMessageEntity();
        BeanUtil.copyProperties(req, entity);
        entity.setId(snowflakeUtil.nextId());
        entity.setStatus(ImMessageStatus.UN_SEND.toString());
        entity.setCreateTime(new Date());
        return entity;
    }

    private ImPersonalMessageEntity savePersonalMessage(PersonalChatMessageReq req) {
        ImPersonalMessageEntity entity = toEntity(req);
        try {
            boolean saved = personalMessageService.save(entity);
            if (!saved) throw new RuntimeException();
        } catch (DuplicateKeyException e) {
            throw new ImException("消息重复");
        } catch (Exception e) {
            throw new ImException("消息持久化失败");
        }
        return entity;
    }

    /**
     * @param req
     */
    @Override
    public ImChatMessageSentResult sendPersonalMessage(PersonalChatMessageReq req) {
        // 保存消息
        ImPersonalMessageEntity entity = savePersonalMessage(req);
        // 发送消息
        ImChatMessage personalMessage = new ImPersonalMessage();
        BeanUtil.copyProperties(entity, personalMessage);
        personalMessage.setSender(new ImUserTerminal(entity.getSenderId(), ImTerminalType.valueOf(entity.getSenderTerminal()), entity.getSenderGroup()));
        List<ImUser> receivers = new LinkedList<>(Arrays.asList(new ImUser(entity.getReceiverId(), entity.getReceiverGroup())));
        personalMessage.setReceivers(receivers);

        return etherImClient.sendChatMessage(personalMessage);
    }

    /**
     * @param req
     */
    @Override
    public ImChatMessageSentResult sendGroupMessage(GroupChatMessageReq req) {
        ImGroupMessage imGroupMessage = new ImGroupMessage();
        imGroupMessage.setSender(req.getSender());
        imGroupMessage.setReceivers(req.getReceivers());
        imGroupMessage.setContent(req.getContent());
        imGroupMessage.setContentType(req.getContentType());
        return etherImClient.sendChatMessage(imGroupMessage);
    }

    /**
     * @param eventLogEntity
     * @return
     */
    @Override
    public boolean saveMessageEventLog(ImMessageEventLogEntity eventLogEntity) {
        return messageEventLogEntityService.save(eventLogEntity);
    }

    /**
     * 对消息事件的处理，持久化消息事件日志，同时同步消息状态
     * 就算消息事件是乱序的，消息的状态也不会被覆盖，始终等于顺序优先级最大的消息事件类型的消息状态
     *
     * @param messageEvent
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void onMessageEvent(ImMessageEvent messageEvent) {
        ChatMessageType messageType = messageEvent.getMessageType();
        if (messageType == ChatMessageType.PERSONAL) {
            ImPersonalMessageEntity messageEntity = personalMessageService.getById(messageEvent.getMessageId());
            if (messageEntity == null) {
                return;
            }
            // 保存消息事件日志
            List<ImMessageEventLogEntity> eventLogs = messageEventLogEntityService.lambdaQuery()
                    .eq(ImMessageEventLogEntity::getMessageId, messageEntity.getId())
                    .list();
            // 判断是否已存在
            boolean present = eventLogs.stream()
                    .anyMatch((log) -> log.getEventType().equals(messageEvent.getEventType().toString()));
            if (present) {
                return;
            }
            ImMessageEventLogEntity eventLogEntity = new ImMessageEventLogEntity();
            eventLogEntity.setMessageId(messageEntity.getId());
            eventLogEntity.setEventType(messageEvent.getEventType().toString());
            eventLogEntity.setEventTime(messageEvent.getEventTime());
            eventLogEntity.setCreateTime(new Date());
            try {
                messageEventLogEntityService.save(eventLogEntity);
            } catch (DuplicateKeyException e) {
                // 捕获重复插入异常，避免重复消费
                log.warn("消息事件日志重复,eventLogEntity:{}", JSON.toJSONString(eventLogEntity));
                return;
            }

            // 更新消息状态
            eventLogs = messageEventLogEntityService.lambdaQuery()
                    .eq(ImMessageEventLogEntity::getMessageId, messageEntity.getId())
                    .list();
            List<ImMessageEventLogEntity> orderedEventList = new LinkedList<>();
            for (ImMessageEventLogEntity log : eventLogs) {
                ImMessageEventType eventType = ImMessageEventType.valueOf(log.getEventType());
                orderedEventList.add(eventType.getOrder(), log);
            }
            if (CollectionUtil.isNotEmpty(orderedEventList)) {
                ImMessageEventLogEntity newestLog = orderedEventList.get(orderedEventList.size() - 1);
                ImMessageStatus status = ImMessageEventType.valueOf(newestLog.getEventType()).getStatus();
                messageEntity.setStatus(status.name());
                personalMessageService.updateById(messageEntity);
            }
        }
    }
}
