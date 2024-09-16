package cn.ether.im.message.service;

import cn.ether.im.message.dto.GroupChatMessageReq;
import cn.ether.im.message.dto.PersonalChatMessageReq;

/**
 * * @Author: Martin
 * * @Date    2024/9/15 14:57
 * * @Description
 **/
public interface MessageService {

    public String sendPersonalMessage(PersonalChatMessageReq req);

    public String sendGroupMessage(GroupChatMessageReq req);

}
