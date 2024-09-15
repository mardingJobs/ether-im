package cn.ether.im.sdk.sender.impl;

import cn.ether.im.common.helper.ImHelper;
import cn.ether.im.common.model.message.ImPersonalMessage;
import cn.ether.im.common.model.message.ImTopicMessage;
import cn.ether.im.common.model.user.ImUser;
import cn.ether.im.common.mq.ImMessageSender;
import cn.ether.im.sdk.sender.ChatMessageSender;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * * @Author: Martin
 * * @Date    2024/9/15 15:23
 * * @Description
 **/
@Component
public class DefaultChatMessageSender implements ChatMessageSender {

    @Resource
    private ImHelper helper;

    @Resource
    private ImMessageSender messageSender;

    /**
     * 向多个用户终端发送消息
     *
     * @param personalMessage
     */
    @Override
    public void sendPersonalMessage(ImPersonalMessage personalMessage) {
        ImUser user = personalMessage.getReceivers().get(0);
        List<String> topics = helper.relatedTopic(user);
        if (CollectionUtils.isEmpty(topics)) {
            return;
        }
        topics.forEach((topic) -> {
            messageSender.send(new ImTopicMessage(personalMessage, topic));
        });
    }

    /**
     * @param personalMessage
     */
    @Override
    public void sendGroupMessage(ImPersonalMessage personalMessage) {

    }
}
