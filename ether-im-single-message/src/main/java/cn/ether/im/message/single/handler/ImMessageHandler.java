package cn.ether.im.message.single.handler;

import cn.ether.im.message.single.model.dto.MessageSendReq;

/**
 * * @Author: Martin
 * * @Date    2024/9/15 14:57
 * * @Description
 **/
public interface ImMessageHandler {

    /**
     * 同步发送单聊消息
     *
     * @param req
     * @return
     * @throws Exception
     */
    String sendMessage(MessageSendReq req) throws Exception;

    /**
     * 异步发送单聊消息
     *
     * @param req
     * @throws Exception
     */
    void asyncSendMessage(MessageSendReq req) throws Exception;

    /**
     * 发送消息已读通知
     *
     * @param messageId
     */
    void sendMessageReadNotice(String messageId);

    /**
     * 发送消息已撤回通知
     *
     * @param messageId
     */
    void sendMessageWithDrawnNotice(String messageId);

}
