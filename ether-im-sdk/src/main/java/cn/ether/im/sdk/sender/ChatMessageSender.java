package cn.ether.im.sdk.sender;

import cn.ether.im.common.model.message.ImChatMessage;

/**
 * * @Author: Martin
 * * @Date    2024/9/15 15:22
 * * @Description
 **/
public interface ChatMessageSender {

    /**
     * 发送聊天消息，支持单聊和群聊，支持发送者多端同步
     *
     * @param chatMessage
     */
    void sendChatMessage(ImChatMessage chatMessage);

}
