package cn.ether.im.sdk.sender.impl;

import cn.ether.im.common.helper.ImUserCacheHelper;
import cn.ether.im.common.model.message.ImChatMessage;
import cn.ether.im.common.model.message.ImTopicMessage;
import cn.ether.im.common.model.user.ImUser;
import cn.ether.im.common.model.user.ImUserTerminal;
import cn.ether.im.common.mq.ImMessageSender;
import cn.ether.im.sdk.sender.ChatMessageSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

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


    @Override
    public void sendChatMessage(ImChatMessage chatMessage) {
        ImUserTerminal sender = chatMessage.getSender();
        List<ImUser> receivers = chatMessage.getReceivers();

        Map<String, List<ImUserTerminal>> topicTerminalMap = new HashMap<>();
        List<ImUserTerminal> terminalList = receivers.stream()
                .map(receiver -> userCacheHelper.onlineTerminals(receiver))
                .flatMap(Collection::stream)
                .filter(terminal -> !terminal.equals(sender))
                .collect(Collectors.toList());

        terminalList.stream()
                .forEach((terminal) -> {
                    List<String> topics = userCacheHelper.relatedTopic(terminal);
                    for (String topic : topics) {
                        topicTerminalMap.computeIfAbsent(topic, k -> new LinkedList<>()).add(terminal);
                    }
                });

        topicTerminalMap.forEach((topic, terminals) -> {
            chatMessage.setReceiverTerminals(terminals);
            messageSender.send(new ImTopicMessage(chatMessage, topic));
        });
    }
}
