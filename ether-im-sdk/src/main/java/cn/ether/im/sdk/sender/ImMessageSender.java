package cn.ether.im.sdk.sender;

import cn.ether.im.common.enums.ImMessageSendResult;
import cn.ether.im.common.model.message.ImMessage;
import cn.ether.im.common.model.message.ImSingleMessage;

/**
 * * @Author: Martin
 * * @Date    2024/9/15 15:22
 * * @Description
 **/
public interface ImMessageSender {

    /**
     * 发送消息，支持单聊和群聊，支持发送者多端同步
     *
     * @param chatMessage
     * @param async 是否是异步发送
     */
    void sendMessage(ImMessage chatMessage, boolean async) throws Exception;


    ImMessageSendResult sendSingleMessage(ImSingleMessage singleMessage);
}
