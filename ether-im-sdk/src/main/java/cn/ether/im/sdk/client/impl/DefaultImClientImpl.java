package cn.ether.im.sdk.client.impl;

import cn.ether.im.common.enums.ImExceptionCode;
import cn.ether.im.common.exception.ImException;
import cn.ether.im.common.helper.ImUserContextHelper;
import cn.ether.im.common.model.message.ImChatMessage;
import cn.ether.im.common.model.user.ImUser;
import cn.ether.im.sdk.client.EtherImClient;
import cn.ether.im.sdk.sender.ChatMessageSender;
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
    private ChatMessageSender messageSender;

    @Resource
    private ImUserContextHelper contextHelper;


    /**
     * 发送消息
     *
     * @param chatMessage
     */
    @Override
    public void sendChatMessage(ImChatMessage chatMessage) throws Exception {
        messageSender.sendChatMessage(chatMessage);
    }

    @Override
    public void asyncSendChatMessage(ImChatMessage chatMessage) throws Exception {
        messageSender.asyncSendChatMessage(chatMessage);
    }

    @Override
    public boolean isOnline(ImUser user) {
        Map<String, String> connectedServerIds = contextHelper.getConnectionInfo(user);
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
        return contextHelper.getUserIdsByGroupId(groupId);
    }

}
