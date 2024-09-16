package cn.ether.im.sdk.client;

import cn.ether.im.common.model.message.ImChatMessage;
import cn.ether.im.common.model.user.ImUserTerminal;
import cn.ether.im.common.util.SnowflakeUtil;
import cn.ether.im.sdk.sender.ChatMessageSender;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

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
    private SnowflakeUtil snowflakeUtil;


    /**
     * 发送单聊消息
     *
     * @param chatMessage
     */
    @Override
    public String sendChatMessage(ImChatMessage chatMessage) {
        // 设置消息ID
        if (StringUtils.isEmpty(chatMessage.getId())) {
            String id = snowflakeUtil.nextId();
            chatMessage.setId(id);
        }
        // 设置时间
        if (chatMessage.getTimestamp() == null) {
            chatMessage.setTimestamp(System.currentTimeMillis());
        }
        // 给自己其他终端发送消息
        ImUserTerminal sender = chatMessage.getSender();
        chatMessage.getReceivers().add(sender.cloneUser());
        messageSender.sendChatMessage(chatMessage);
        return chatMessage.getId();
    }

}
