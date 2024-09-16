package cn.ether.im.sdk.sender.impl;

import cn.ether.im.common.enums.ImTerminalType;
import cn.ether.im.common.helper.ImUserCacheHelper;
import cn.ether.im.common.model.message.ImPersonalMessage;
import cn.ether.im.common.model.message.ImTopicMessage;
import cn.ether.im.common.model.user.ImUser;
import cn.ether.im.common.model.user.ImUserTerminal;
import cn.ether.im.common.mq.ImMessageSender;
import cn.ether.im.sdk.sender.ChatMessageSender;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * * @Author: Martin
 * * @Date    2024/9/15 15:23
 * * @Description
 **/
@Slf4j
@Component
public class DefaultChatMessageSender implements ChatMessageSender {

    @Resource
    private ImUserCacheHelper userCacheHelper;

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
        List<ImTerminalType> onlineTerminals = userCacheHelper.onlineTerminalTypes(user);
        if (CollectionUtils.isEmpty(onlineTerminals)) {
            log.info("Receiver is offline.");
            return;
        }
        for (ImTerminalType onlineTerminal : onlineTerminals) {
            personalMessage.getReceiverTerminals().add(new ImUserTerminal(user, onlineTerminal));
        }
        List<String> topics = userCacheHelper.relatedTopic(user);
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
