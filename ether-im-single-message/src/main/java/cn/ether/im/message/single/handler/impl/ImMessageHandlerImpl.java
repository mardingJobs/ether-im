package cn.ether.im.message.single.handler.impl;

import cn.ether.im.common.mq.ImMqMessageSender;
import cn.ether.im.common.util.SnowflakeUtil;
import cn.ether.im.message.single.handler.ImMessageHandler;
import cn.ether.im.message.single.model.dto.MessageSendReq;
import cn.ether.im.message.single.service.ImSingleMessageEtService;
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
    private ImSingleMessageEtService singleMessageService;

    @Override
    public String sendMessage(MessageSendReq req) throws Exception {
        //etherImClient.sendChatMessage();
        return "";
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
