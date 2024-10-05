package cn.ether.im.sdk.sender.impl;

import cn.ether.im.common.cache.ImUserContextCache;
import cn.ether.im.common.constants.ImConstants;
import cn.ether.im.common.enums.ImMessageSendResult;
import cn.ether.im.common.enums.ImMessageType;
import cn.ether.im.common.model.info.ImTopicInfo;
import cn.ether.im.common.model.message.ImMessage;
import cn.ether.im.common.model.message.ImSingleMessage;
import cn.ether.im.common.model.user.ImUser;
import cn.ether.im.common.mq.ImMqMessageSender;
import cn.ether.im.sdk.sender.ImMessageSender;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * * @Author: Martin
 * * @Date    2024/9/15 15:23
 * * @Description
 **/
@Slf4j
@Component
public class DefaultImMessageSender implements ImMessageSender {

    @Resource
    private ImUserContextCache userContextCache;

    @Resource
    private ImMqMessageSender messageSender;


    @Override
    public void sendMessage(ImMessage message, boolean async) throws Exception {
//        ImUserTerminal sender = message.getSender();
//        List<ImUser> receivers = message.getReceivers();
//        List<ImUserTerminal> targetTerminalList = onlineTerminals(receivers, null);
//        // 获取自己其他在线终端
//        if (ImMessageType.SINGLE.equals(message.getMessageType())) {
//            List<ImUserTerminal> otherSelfTerminals = onlineTerminals(Collections.singletonList(sender.cloneUser()), sender);
//            targetTerminalList.addAll(otherSelfTerminals);
//        }
//        doSendToTargetTerminal(message, targetTerminalList, async);
    }

    @Override
    public ImMessageSendResult sendSingleMessage(ImSingleMessage singleMessage) {
        ImTopicInfo<ImSingleMessage> topicInfo = new ImTopicInfo<>();
        topicInfo.setTopic(ImConstants.IM_CHAT_MESSAGE_TOPIC);
        List<String> serverIds = userContextCache.connectedServerIds(new ImUser(singleMessage.getReceiverId()));
        if (serverIds.isEmpty()) {
            return ImMessageSendResult.RECEIVER_NOT_ONLINE;
        }
        topicInfo.setTag(ImConstants.IM_CHAT_MESSAGE_TAG_PREFIX + serverIds.get(0));
        topicInfo.setMessage(singleMessage);
        // 等用事物消息
        TransactionSendResult transactionSendResult = messageSender.sendMessageInTransaction(topicInfo, ImMessageType.SINGLE);
        if (transactionSendResult.getSendStatus() == SendStatus.SEND_OK) {
            return ImMessageSendResult.SUCCESS;
        }
        return ImMessageSendResult.FAIL;
    }

}
