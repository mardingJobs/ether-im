package cn.ether.im.sdk.sender.impl;

import cn.ether.im.common.helper.ImUserContextHelper;
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
    private ImUserContextHelper userCacheHelper;

    @Resource
    private ImMessageSender messageSender;


    @Override
    public boolean sendChatMessage(ImChatMessage chatMessage) {
        try {
            ImUserTerminal sender = chatMessage.getSender();
            List<ImUser> receivers = chatMessage.getReceivers();

            Map<String, List<ImUserTerminal>> topicTerminalMap = new HashMap<>();
            List<ImUserTerminal> terminalList = receivers.stream()
                    .map(receiver -> userCacheHelper.onlineTerminals(receiver))
                    .flatMap(Collection::stream)
                    .filter(terminal -> !terminal.equals(sender))
                    .collect(Collectors.toList());

            if (terminalList.isEmpty()) {
                log.info("无接收终端在线,MessageId:{}", chatMessage.getId());
                return false;
            }

            terminalList.stream()
                    .forEach((terminal) -> {
                        List<String> topics = userCacheHelper.relatedTopic(terminal);
                        for (String topic : topics) {
                            topicTerminalMap.computeIfAbsent(topic, k -> new LinkedList<>()).add(terminal);
                        }
                    });

            topicTerminalMap.forEach((topic, terminals) -> {
                chatMessage.setReceiverTerminals(terminals);
                try {
                    messageSender.send(new ImTopicMessage(chatMessage, topic));
                } catch (Exception e) {
                    log.error("MQ 消息发送失败，消息ID：{}", chatMessage.getId());
                    throw new RuntimeException(e);
                }
            });
        } catch (Exception e) {
            log.error("消息发送失败,消息ID：{}", chatMessage.getId(), e);
            return false;
        }

        return true;
    }
}
