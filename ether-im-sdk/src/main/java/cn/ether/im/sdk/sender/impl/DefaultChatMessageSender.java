package cn.ether.im.sdk.sender.impl;

import cn.ether.im.common.constants.ImConstants;
import cn.ether.im.common.helper.ImUserContextHelper;
import cn.ether.im.common.model.message.ImChatMessage;
import cn.ether.im.common.model.message.ImTopicMessage;
import cn.ether.im.common.model.user.ImUser;
import cn.ether.im.common.model.user.ImUserTerminal;
import cn.ether.im.common.mq.ImMessageSender;
import cn.ether.im.sdk.sender.ChatMessageSender;
import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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

            List<ImUserTerminal> targetTerminalList = onlineTerminals(receivers, null);
            if (targetTerminalList.isEmpty()) {
                log.info("无接收终端在线,MessageId:{}", chatMessage.getId());
                return false;
            }
            doSendToTargetTerminal(chatMessage, targetTerminalList);

            // 发送消息到自己的其他终端
            List<ImUserTerminal> otherSelfTerminals = onlineTerminals(Collections.singletonList(sender.cloneUser()), sender);
            doSendToTargetTerminal(chatMessage, otherSelfTerminals);
        } catch (Exception e) {
            log.error("消息发送失败,消息ID：{}", chatMessage.getId(), e);
            return false;
        }
        return true;
    }

    private void doSendToTargetTerminal(ImChatMessage chatMessage, List<ImUserTerminal> onlineTerminals) throws Exception {
        Map<String, List<ImUserTerminal>> messageTagMap = onlineTerminals.stream()
                .collect(Collectors.groupingBy((terminal) -> userCacheHelper.getMessageTag(terminal)));

        List<ImTopicMessage> topicMessages = messageTagMap.entrySet().stream().map((entry) -> {
            ImChatMessage newChatMessage = new ImChatMessage();
            BeanUtil.copyProperties(chatMessage, newChatMessage);
            newChatMessage.setReceiverTerminals(entry.getValue());
            return new ImTopicMessage<>(newChatMessage, ImConstants.IM_CHAT_MESSAGE_TOPIC, entry.getKey());
        }).collect(Collectors.toList());

        boolean send = messageSender.batchSend(topicMessages);
        if (send) {
            log.info("发送对话MQ消息成功，TopicMessage:{}", JSON.toJSONString(topicMessages));
        } else {
            log.error("发送对话MQ消息失败，TopicMessage:{}", JSON.toJSONString(topicMessages));
        }
    }


    private List<ImUserTerminal> onlineTerminals(List<ImUser> receivers, ImUserTerminal excludeTerminal) {
        List<ImUserTerminal> terminalList = receivers.stream()
                .map(receiver -> userCacheHelper.onlineTerminals(receiver))
                .flatMap(Collection::stream)
                .filter(terminal -> !terminal.equals(excludeTerminal))
                .collect(Collectors.toList());
        return terminalList;
    }
}
