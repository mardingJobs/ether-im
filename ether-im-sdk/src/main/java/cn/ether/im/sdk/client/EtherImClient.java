package cn.ether.im.sdk.client;

import cn.ether.im.common.model.message.ImChatMessage;
import cn.ether.im.common.model.user.ImUser;

/**
 * * @Author: jack
 * * @Date    2024/9/15 15:16
 * * @Description
 **/
public interface EtherImClient {

    /**
     * 发送对话消息
     *
     * @param chatMessage
     */
    void sendChatMessage(ImChatMessage chatMessage) throws Exception;

    /**
     * 判断用户是否在线
     */
    boolean isOnline(ImUser user);

}
