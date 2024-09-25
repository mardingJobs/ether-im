package cn.ether.im.sdk.client.impl;

import cn.ether.im.common.helper.ImUserContextHelper;
import cn.ether.im.common.model.message.ImChatMessage;
import cn.ether.im.common.model.user.ImUser;
import cn.ether.im.sdk.client.EtherImClient;
import cn.ether.im.sdk.sender.ChatMessageSender;
import cn.hutool.core.collection.CollectionUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
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
    public boolean isOnline(ImUser user) {
        Map<String, String> connectedServerIds = contextHelper.getConnectionInfo(user);
        return CollectionUtil.isNotEmpty(connectedServerIds);
    }

}
