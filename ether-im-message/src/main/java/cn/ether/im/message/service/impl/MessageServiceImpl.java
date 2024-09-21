package cn.ether.im.message.service.impl;


import cn.ether.im.common.enums.*;
import cn.ether.im.common.exception.ImException;
import cn.ether.im.common.model.ImChatMessageSentResult;
import cn.ether.im.common.model.message.*;
import cn.ether.im.common.model.user.ImUser;
import cn.ether.im.common.model.user.ImUserTerminal;
import cn.ether.im.common.mq.ImMessageSender;
import cn.ether.im.common.util.SnowflakeUtil;
import cn.ether.im.message.model.dto.GroupChatMessageReq;
import cn.ether.im.message.model.dto.PersonalChatMessageReq;
import cn.ether.im.message.model.entity.ImMessageEventLogEntity;
import cn.ether.im.message.model.entity.ImPersonalMessageEntity;
import cn.ether.im.message.service.ImMessageEventLogEntityService;
import cn.ether.im.message.service.ImPersonalMessageService;
import cn.ether.im.message.service.MessageService;
import cn.ether.im.sdk.client.EtherImClient;
import cn.ether.im.sdk.listener.MessageEventStatusMachine;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
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

    @Resource
    private ImMessageSender messageSender;


    private ImPersonalMessageEntity toEntity(PersonalChatMessageReq req) {
        ImPersonalMessageEntity entity = new ImPersonalMessageEntity();
        BeanUtil.copyProperties(req, entity);
        entity.setId(snowflakeUtil.nextId());
        entity.setStatus(ImMessageStatus.INTI.name());
        entity.setCreateTime(new Date());
        return entity;
    }

    private ImPersonalMessageEntity savePersonalMessage(PersonalChatMessageReq req) {
        ImPersonalMessageEntity entity = toEntity(req);
        try {
            boolean saved = personalMessageService.save(entity);
            if (!saved) throw new RuntimeException();
        } catch (DuplicateKeyException e) {
            throw new ImException(ImExceptionCode.MESSAGE_DUPLICATION);
        } catch (Exception e) {
            throw new ImException(ImExceptionCode.MESSAGE_PERSIST_FAIL);
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
        List<ImUser> receivers = new LinkedList<>(Arrays.asList(new ImUser(entity.getReceiverId())));
        personalMessage.setReceivers(receivers);

        boolean sent = etherImClient.sendChatMessage(personalMessage);
        if (sent) {
            entity.setStatus(ImMessageStatus.SENT.name());
            return ImChatMessageSentResult.success(entity.getId());
        } else {
            log.error("发送消息失败|参数:{}", JSON.toJSONString(personalMessage));
            entity.setStatus(ImMessageStatus.SENT_FAIL.name());
            personalMessageService.updateById(entity);
            return ImChatMessageSentResult.sentFail(entity.getId());
        }
    }

    /**
     * 发送个人消息，使用事务保证保存消息到数据库和发送MQ消息一致性
     *
     * @param req
     * @return
     */
    @Override
    public Long sendPersonalMessageTransaction(PersonalChatMessageReq req) {
        ImPersonalMessageEntity entity = toEntity(req);

        ImChatMessage personalMessage = new ImPersonalMessage();
        BeanUtil.copyProperties(entity, personalMessage);
        personalMessage.setSender(new ImUserTerminal(entity.getSenderId(), ImTerminalType.valueOf(entity.getSenderTerminal()), entity.getSenderGroup()));
        List<ImUser> receivers = new LinkedList<>(Arrays.asList(new ImUser(entity.getReceiverId())));
        personalMessage.setReceivers(receivers);
        // 组装事物消息
        ImTopicMessage<ImChatMessage> topicMessage = new ImTopicMessage<>(personalMessage, "im-chat-message-tx-topic", "");
        TransactionSendResult sendResult = messageSender.sendMessageInTransaction(topicMessage, null);
        if (sendResult.getSendStatus() != SendStatus.SEND_OK) {
            log.error("发送事务消息失败|参数:{}", JSON.toJSONString(topicMessage));
        }
        return entity.getId();
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
        boolean sent = etherImClient.sendChatMessage(imGroupMessage);
        return sent ? ImChatMessageSentResult.success(imGroupMessage.getId()) : ImChatMessageSentResult.sentFail(imGroupMessage.getId());
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
     * 1: 加分布式锁，保证相同消息顺序处理
     * 2: 使用状态模式进行重构
     *
     * @param messageEvent
     */
    @Override
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    public void onMessageEvent(ImMessageEvent messageEvent) {

        ImChatMessageType messageType = messageEvent.getMessageType();
        if (messageType == ImChatMessageType.PERSONAL) {
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
            saveMessageEventLog(messageEvent);

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
                ImMessageStatus status = ImMessageEventType.valueOf(newestLog.getEventType()).getNextStatus();
                messageEntity.setStatus(status.name());
                personalMessageService.updateById(messageEntity);
            }
        }
    }

    /**
     * 对于重复消息，采用忽略的策略，如果采用退回MQ的策略，会导致消息一直无法消费成功。
     * 对于乱序的消息，比如 未发送状态+推送事件，也采用忽略 + 状态纠正的策略。
     * 状态纠正指的是在更新消息状态时，比较消息事件日志表的数据，如果事件日志表中存在本该后面发生的事件A，则更新为事件A发生后的状态
     * 对于乱序的消息，理论上应该采用退回MQ的策略，但是无法区分消息是重复消息还是乱序消息。
     *
     * @param messageEvent
     */
    @Override
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    public void onMessageEventV2(ImMessageEvent messageEvent) {
        ImPersonalMessageEntity messageEntity = personalMessageService.getById(messageEvent.getMessageId());
        if (messageEntity == null) {
            return;
        }
        String status = messageEntity.getStatus();
        ImMessageStatus messageStatus = ImMessageStatus.valueOf(status);

        ImMessageStatus nextStatus = MessageEventStatusMachine.nextStatus(messageStatus, messageEvent.getEventType());
        if (nextStatus == null) {
            saveMessageEventLog(messageEvent);
            return;
        }

        // 校验nextStatus 是否是 事件日志中的最新状态
        ImMessageEventLogEntity lastEventLogEntity = messageEventLogEntityService.lambdaQuery()
                .eq(ImMessageEventLogEntity::getMessageId, messageEntity.getId())
                .orderByDesc(ImMessageEventLogEntity::getEventTypeOrder)
                .last("limit 1")
                .one();

        if (lastEventLogEntity != null) {
            if (lastEventLogEntity.getEventTypeOrder() >= messageEvent.getEventType().getOrder()) {
                // 说明这个事件消息晚发或者重发了,不用更新这个事件的状态
                return;
            }
        }
        messageEntity.setStatus(nextStatus.name());
        personalMessageService.updateById(messageEntity);
    }

    private void saveMessageEventLog(ImMessageEvent messageEvent) {
        ImMessageEventLogEntity eventLogEntity = new ImMessageEventLogEntity();
        eventLogEntity.setMessageId(messageEvent.getMessageId());
        eventLogEntity.setEventType(messageEvent.getEventType().name());
        eventLogEntity.setEventTime(messageEvent.getEventTime());
        eventLogEntity.setEventTypeOrder(messageEvent.getEventType().getOrder());
        eventLogEntity.setCreateTime(new Date());
        try {
            messageEventLogEntityService.save(eventLogEntity);
        } catch (DuplicateKeyException e) {
            // 捕获重复插入异常，避免重复消费
            log.warn("消息事件日志重复,eventLogEntity:{}", JSON.toJSONString(eventLogEntity));
        }
    }

}
