package cn.ether.im.message.service;

import cn.ether.im.common.model.ImChatMessageSentResult;
import cn.ether.im.common.model.message.ImMessageEvent;
import cn.ether.im.message.model.dto.ChatMessagePullReq;
import cn.ether.im.message.model.dto.ChatMessagePullResult;
import cn.ether.im.message.model.dto.ChatMessageSendReq;
import cn.ether.im.message.model.entity.ImMessageEventLogEntity;

/**
 * * @Author: Martin
 * * @Date    2024/9/15 14:57
 * * @Description
 **/
public interface MessageService {

    /**
     * 发送消息,先保存消息，然后发送MQ消息
     * 保存消息成功,消息状态是INIT
     * 如果发送MQ消息失败，消息状态是SENT_FAIL
     * 如果消息发送成功，消息状态是SENT
     * 对于消息发送失败的消息，这里不处理，由调用方自己处理
     *
     * @param req
     * @return
     */
    ImChatMessageSentResult sendMessage(ChatMessageSendReq req);

    /**
     * @return
     */
    ChatMessagePullResult pullRecentMessages(ChatMessagePullReq pullReq);

    /**
     * 发送消息，使用事务保证保存消息到数据库和发送MQ消息一致性
     *
     * @param req
     * @return 消息ID
     */
    Long sendMessageTransaction(ChatMessageSendReq req);


    boolean saveMessageEventLog(ImMessageEventLogEntity eventLogEntity);

    void onMessageEvent(ImMessageEvent messageEvent);

    void onMessageEventV2(ImMessageEvent messageEvent);




}
