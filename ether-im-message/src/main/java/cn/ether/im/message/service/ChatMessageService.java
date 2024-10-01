package cn.ether.im.message.service;

import cn.ether.im.common.model.info.message.event.ImMessageEvent;
import cn.ether.im.message.model.dto.*;
import cn.ether.im.message.model.entity.ImMessageEventLogEntity;

/**
 * * @Author: Martin
 * * @Date    2024/9/15 14:57
 * * @Description
 **/
public interface ChatMessageService {


    String sendPersonalMessage(PersonalMessageSendReq req) throws Exception;

    void asyncSendPersonalMessage(PersonalMessageSendReq req) throws Exception;


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


    String sendGroupMessage(GroupMessageSendReq req) throws Exception;


}
