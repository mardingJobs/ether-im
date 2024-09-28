package cn.ether.im.sdk.sender.impl;

import cn.ether.im.common.constants.ImConstants;
import cn.ether.im.common.enums.ImChatMessageType;
import cn.ether.im.common.enums.ImExceptionCode;
import cn.ether.im.common.exception.ImException;
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
    public void sendChatMessage(ImChatMessage chatMessage) throws Exception {
        ImUserTerminal sender = chatMessage.getSender();
        List<ImUser> receivers = chatMessage.getReceivers();
        List<ImUserTerminal> targetTerminalList = onlineTerminals(receivers, null);
        // 获取自己其他在线终端
        if (ImChatMessageType.PERSONAL.equals(chatMessage.getChatMessageType())) {
            List<ImUserTerminal> otherSelfTerminals = onlineTerminals(Collections.singletonList(sender.cloneUser()), sender);
            targetTerminalList.addAll(otherSelfTerminals);
        }
        doSendToTargetTerminal(chatMessage, targetTerminalList);
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
        if (topicMessages.isEmpty()) {
            return;
        }
        boolean send = messageSender.batchSend(topicMessages);
        if (!send) {
            throw new ImException(ImExceptionCode.MESSAGE_SENT_TO_MQ_FAIL);
        }
        if (log.isDebugEnabled()) {
            log.debug("已发送消息到MQ：{}", JSON.toJSONString(topicMessages));
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
