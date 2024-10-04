package cn.ether.im.sdk.client.impl;

import cn.ether.im.common.cache.ImUserContextCache;
import cn.ether.im.common.enums.ImExceptionCode;
import cn.ether.im.common.exception.ImException;
import cn.ether.im.common.model.message.ImMessage;
import cn.ether.im.common.model.message.ImSingleMessage;
import cn.ether.im.common.model.user.ImUser;
import cn.ether.im.sdk.cache.MessageRemoteCache;
import cn.ether.im.sdk.client.EtherImClient;
import cn.ether.im.sdk.sender.ImMessageSender;
import cn.hutool.core.collection.CollectionUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * * @Author: Martin
 * * @Date    2024/9/15 15:19
 * * @Description
 **/
@Component
public class DefaultImClientImpl implements EtherImClient {


    @Resource
    private ImMessageSender messageSender;

    @Resource
    private ImUserContextCache userContextCache;

    @Resource
    private MessageRemoteCache messageRemoteCache;


    @Override
    public void sendSingleMessage(ImSingleMessage singleMessage) {
        messageSender.sendSingleMessage();
    }

    /**
     * 发送消息
     *
     * @param chatMessage
     */
    @Override
    public void sendChatMessage(ImMessage chatMessage) throws Exception {
        messageSender.sendMessage(chatMessage, false);
    }

    @Override
    public void asyncSendChatMessage(ImMessage chatMessage) throws Exception {
        messageSender.sendMessage(chatMessage, true);
    }

    @Override
    public boolean isOnline(ImUser user) {
        Map<String, String> connectedServerIds = userContextCache.getConnectionInfo(user);
        return CollectionUtil.isNotEmpty(connectedServerIds);
    }

    @Override
    public void requireReceiverOnline(String receiverId) {
        // 先判断对方是否在线
        boolean online = this.isOnline(new ImUser(receiverId));
        if (!online) {
            throw new ImException(ImExceptionCode.RECEIVER_NOT_ONLINE);
        }
    }

    @Override
    public List<String> getOnlineGroupMembers(String groupId) {
        return userContextCache.getUserIdsByGroupId(groupId);
    }

}
