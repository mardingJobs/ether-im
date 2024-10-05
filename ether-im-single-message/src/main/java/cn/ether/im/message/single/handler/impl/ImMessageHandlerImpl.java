package cn.ether.im.message.single.handler.impl;

import cn.ether.im.common.enums.ImMessageContentType;
import cn.ether.im.common.model.message.ImSingleMessage;
import cn.ether.im.common.model.user.ImUserTerminal;
import cn.ether.im.common.mq.ImMqMessageSender;
import cn.ether.im.common.util.SnowflakeUtil;
import cn.ether.im.message.single.handler.ImMessageHandler;
import cn.ether.im.message.single.model.dto.MessageSendReq;
import cn.ether.im.message.single.model.session.SessionContext;
import cn.ether.im.message.single.service.ImSingleMessageETService;
import cn.ether.im.sdk.client.EtherImClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 * * @Author: Martin
 * * @Date    2024/9/15 14:58
 * * @Description
 **/
@Slf4j
@Service
public class ImMessageHandlerImpl implements ImMessageHandler {

    @Resource
    private EtherImClient etherImClient;

    @Resource
    private SnowflakeUtil snowflakeUtil;

    @Resource
    private ImMqMessageSender messageSender;

    @Resource
    private ImSingleMessageETService singleMessageService;

    @Override
    public String sendSingleMessage(MessageSendReq req) throws Exception {
        ImUserTerminal loggedUser = SessionContext.loggedUser();
        boolean online = etherImClient.isOnline(req.getReceiverId());
        if (!online) {
            // 保存数据库
        }
        ImSingleMessage singleMessage = new ImSingleMessage();
        Long messageId = snowflakeUtil.nextId();
        singleMessage.setMessageId(messageId);
        singleMessage.setContent(req.getContent());
        singleMessage.setContentType(ImMessageContentType.valueOf(req.getContentType()));
        singleMessage.setSenderId(loggedUser.getUserId());
        singleMessage.setSenderTerminal(loggedUser.getTerminalType());
        singleMessage.setReceiverId(req.getReceiverId());
        singleMessage.setSendTime(System.currentTimeMillis());
        etherImClient.sendSingleMessage(singleMessage);
        return messageId.toString();
    }

    @Override
    public void asyncSendMessage(MessageSendReq req) throws Exception {

    }

    @Override
    public void sendMessageReadNotice(String messageId) {

    }

    @Override
    public void sendMessageWithDrawnNotice(String messageId) {

    }
}
