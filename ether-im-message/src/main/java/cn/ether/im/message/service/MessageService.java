package cn.ether.im.message.service;

import cn.ether.im.common.model.ImChatMessageSentResult;
import cn.ether.im.common.model.message.ImMessageEvent;
import cn.ether.im.message.dto.GroupChatMessageReq;
import cn.ether.im.message.dto.PersonalChatMessageReq;
import cn.ether.im.message.entity.ImMessageEventLogEntity;

/**
 * * @Author: Martin
 * * @Date    2024/9/15 14:57
 * * @Description
 **/
public interface MessageService {

    /**
     * 发送个人消息,先保存消息，然后发送MQ消息
     * 保存消息成功,消息状态是INIT
     * 如果发送MQ消息失败，消息状态是SENT_FAIL
     * 如果消息发送成功，消息状态是SENT
     * 对于消息发送失败的消息，可以通过定时任务重试
     *
     * @param req
     * @return
     */
    ImChatMessageSentResult sendPersonalMessage(PersonalChatMessageReq req);

    /**
     * 发送个人消息，使用事务保证保存消息到数据库和发送MQ消息一致性
     *
     * @param req
     * @return 消息ID
     */
    Long sendPersonalMessageTransaction(PersonalChatMessageReq req);

    ImChatMessageSentResult sendGroupMessage(GroupChatMessageReq req);

    boolean saveMessageEventLog(ImMessageEventLogEntity eventLogEntity);

    void onMessageEvent(ImMessageEvent messageEvent);

    void onMessageEventV2(ImMessageEvent messageEvent);



}
