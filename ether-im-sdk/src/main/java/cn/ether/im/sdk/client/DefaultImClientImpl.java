package cn.ether.im.sdk.client;

import cn.ether.im.common.model.ImChatMessageSentResult;
import cn.ether.im.common.model.message.ImChatMessage;
import cn.ether.im.common.model.user.ImUserTerminal;
import cn.ether.im.common.util.SnowflakeUtil;
import cn.ether.im.sdk.sender.ChatMessageSender;
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
     * 发送消息
     *
     * @param chatMessage
     */
    @Override
    public ImChatMessageSentResult sendChatMessage(ImChatMessage chatMessage) {
        // 给自己其他终端发送消息
        ImUserTerminal sender = chatMessage.getSender();
        chatMessage.getReceivers().add(sender.cloneUser());
        boolean send = messageSender.sendChatMessage(chatMessage);
        if (send) {
            return ImChatMessageSentResult.success(chatMessage.getId());
        }
        return ImChatMessageSentResult.fail(chatMessage.getId());
    }

}
