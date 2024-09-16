package cn.ether.im.sdk.sender.impl;

import cn.ether.im.common.helper.ImUserCacheHelper;
import cn.ether.im.common.model.message.ImGroupMessage;
import cn.ether.im.common.model.message.ImPersonalMessage;
import cn.ether.im.common.model.message.ImTopicMessage;
import cn.ether.im.common.model.user.ImUser;
import cn.ether.im.common.model.user.ImUserTerminal;
import cn.ether.im.common.mq.ImMessageSender;
import cn.ether.im.sdk.sender.ChatMessageSender;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

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
        ImUser receiver = personalMessage.getReceivers().get(0);
        List<ImUserTerminal> receiverTerminals = userCacheHelper.onlineTerminals(receiver);
        if (CollectionUtils.isEmpty(receiverTerminals)) {
            log.info("Receiver is offline.receiver:{}", JSON.toJSONString(receiver));
            return;
        }
        personalMessage.setReceiverTerminals(receiverTerminals);
        // 发送消息
        List<String> topics = userCacheHelper.relatedTopic(receiver);
        topics.forEach((topic) -> {
            messageSender.send(new ImTopicMessage(personalMessage, topic));
        });
    }

    /**
     * @param groupMessage
     */
    @Override
    public void sendGroupMessage(ImGroupMessage groupMessage) {
        List<ImUser> receivers = groupMessage.getReceivers();

        Map<String, List<ImUserTerminal>> topicTerminalMap = new HashMap<>();
        receivers.stream()
                .map(receiver -> userCacheHelper.onlineTerminals(receiver))
                .flatMap(Collection::stream)
                .forEach((terminal) -> {
                    List<String> topics = userCacheHelper.relatedTopic(terminal);
                    for (String topic : topics) {
                        topicTerminalMap.computeIfAbsent(topic, k -> new LinkedList<>()).add(terminal);
                    }
                });

        topicTerminalMap.forEach((topic, terminals) -> {
            groupMessage.setReceiverTerminals(terminals);
            messageSender.send(new ImTopicMessage(groupMessage, topic));
        });
    }
}
