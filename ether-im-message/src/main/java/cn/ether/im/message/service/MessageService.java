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

    ImChatMessageSentResult sendPersonalMessage(PersonalChatMessageReq req);

    ImChatMessageSentResult sendGroupMessage(GroupChatMessageReq req);

    boolean saveMessageEventLog(ImMessageEventLogEntity eventLogEntity);

    void onMessageEvent(ImMessageEvent messageEvent);

    void onMessageEventV2(ImMessageEvent messageEvent);



}
