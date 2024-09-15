package cn.ether.im.sdk.client;

import cn.ether.im.common.model.message.ImGroupMessage;
import cn.ether.im.common.model.message.ImPersonalMessage;
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


    /**
     * 发送单聊消息
     *
     * @param personalMessage
     */
    @Override
    public void sendPersonalMessage(ImPersonalMessage personalMessage) {
        messageSender.sendPersonalMessage(personalMessage);
    }

    /**
     * 发送群聊消息
     *
     * @param groupMessage
     */
    @Override
    public void sendGroupMessage(ImGroupMessage groupMessage) {

    }
}
