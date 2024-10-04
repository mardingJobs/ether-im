package cn.ether.im.sdk.client;

import cn.ether.im.common.model.message.ImMessage;
import cn.ether.im.common.model.message.ImSingleMessage;
import cn.ether.im.common.model.user.ImUser;

import java.util.List;

/**
 * * @Author: jack
 * * @Date    2024/9/15 15:16
 * * @Description
 **/
public interface EtherImClient {


    void sendSingleMessage(ImSingleMessage singleMessage);

    /**
     * 发送对话消息
     *
     * @param chatMessage
     */
    void sendChatMessage(ImMessage chatMessage) throws Exception;

    /**
     * 异步发送对话消息
     *
     * @param chatMessage
     */
    void asyncSendChatMessage(ImMessage chatMessage) throws Exception;

    /**
     * 判断用户是否在线
     */
    boolean isOnline(ImUser user);

    /**
     * 校验接受消息的用户是否在线，如果不在线会抛出异常
     * 只能判断用户，不能判断群组
     *
     * @param receiverId
     */
    void requireReceiverOnline(String receiverId);

    /**
     * 获取在线的群组成员
     *
     * @param groupId
     * @return 返回用户ID
     */
    List<String> getOnlineGroupMembers(String groupId);

}
