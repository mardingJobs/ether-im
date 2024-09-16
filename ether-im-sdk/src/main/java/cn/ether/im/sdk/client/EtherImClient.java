package cn.ether.im.sdk.client;

import cn.ether.im.common.model.message.ImChatMessage;

/**
 * * @Author: jack
 * * @Date    2024/9/15 15:16
 * * @Description
 **/
public interface EtherImClient {

    /**
     * 发送单聊消息
     *
     * @param chatMessage
     */
    String sendChatMessage(ImChatMessage chatMessage);


}
