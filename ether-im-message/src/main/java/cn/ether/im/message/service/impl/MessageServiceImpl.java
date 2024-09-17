package cn.ether.im.message.service.impl;


import cn.ether.im.common.enums.ImMessageStatus;
import cn.ether.im.common.enums.ImTerminalType;
import cn.ether.im.common.exception.ImException;
import cn.ether.im.common.model.ImChatMessageSentResult;
import cn.ether.im.common.model.message.ImChatMessage;
import cn.ether.im.common.model.message.ImGroupMessage;
import cn.ether.im.common.model.message.ImPersonalMessage;
import cn.ether.im.common.model.user.ImUser;
import cn.ether.im.common.model.user.ImUserTerminal;
import cn.ether.im.common.util.SnowflakeUtil;
import cn.ether.im.message.dto.GroupChatMessageReq;
import cn.ether.im.message.dto.PersonalChatMessageReq;
import cn.ether.im.message.entity.ImPersonalMessageEntity;
import cn.ether.im.message.service.ImPersonalMessageService;
import cn.ether.im.message.service.MessageService;
import cn.ether.im.sdk.client.EtherImClient;
import cn.hutool.core.bean.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

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
@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private EtherImClient etherImClient;

    @Resource
    private SnowflakeUtil snowflakeUtil;

    @Resource
    private ImPersonalMessageService personalMessageService;


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
}
