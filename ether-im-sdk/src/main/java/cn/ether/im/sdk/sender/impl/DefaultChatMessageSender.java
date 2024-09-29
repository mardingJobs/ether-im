package cn.ether.im.sdk.sender.impl;

import cn.ether.im.common.constants.ImConstants;
import cn.ether.im.common.enums.ImExceptionCode;
import cn.ether.im.common.enums.ImMessageType;
import cn.ether.im.common.exception.ImException;
import cn.ether.im.common.helper.ImUserContextHelper;
import cn.ether.im.common.model.info.ImTopicInfo;
import cn.ether.im.common.model.info.message.ImMessage;
import cn.ether.im.common.model.user.ImUser;
import cn.ether.im.common.model.user.ImUserTerminal;
import cn.ether.im.common.mq.ImMqMessageSender;
import cn.ether.im.sdk.cache.ImCacheMessage;
import cn.ether.im.sdk.cache.MessageRemoteCache;
import cn.ether.im.sdk.sender.ChatMessageSender;
import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
    private MessageRemoteCache messageRemoteCache;

    @Resource
    private ImMqMessageSender messageSender;

    @Value("${spring.profiles.active:default}")
    private String environmentName;


    private void cacheMessage(ImMessage message) {
        ImCacheMessage cachedMessage = new ImCacheMessage();
        cachedMessage.setId(message.getId());
        cachedMessage.setSenderId(message.getSender().getUserId());
        cachedMessage.setSendTerminal(message.getSender().getTerminalType().name());
        cachedMessage.setContent(message.getContent());
        cachedMessage.setContentType(message.getContentType().name());
        cachedMessage.setChatMessageType(message.getChatMessageType());
        cachedMessage.setSendTime(message.getSendTime());
        cachedMessage.setReceiverId(message.getReceivers().get(0).getUserId());
        messageRemoteCache.putMessage(cachedMessage);
    }

    @Override
    public void sendChatMessage(ImMessage message, boolean async) throws Exception {
        cacheMessage(message);
        ImUserTerminal sender = message.getSender();
        List<ImUser> receivers = message.getReceivers();
        List<ImUserTerminal> targetTerminalList = onlineTerminals(receivers, null);
        // 获取自己其他在线终端
        if (ImMessageType.PERSONAL.equals(message.getChatMessageType())) {
            List<ImUserTerminal> otherSelfTerminals = onlineTerminals(Collections.singletonList(sender.cloneUser()), sender);
            targetTerminalList.addAll(otherSelfTerminals);
        }
        doSendToTargetTerminal(message, targetTerminalList, async);
    }


    private void doSendToTargetTerminal(ImMessage chatMessage, List<ImUserTerminal> onlineTerminals, boolean async) throws Exception {
        Map<String, List<ImUserTerminal>> messageTagMap = onlineTerminals.stream()
                .collect(Collectors.groupingBy((terminal) -> userCacheHelper.getMessageTag(terminal)));

        List<ImTopicInfo> topicMessages = messageTagMap.entrySet().stream().map((entry) -> {
            ImMessage newChatMessage = new ImMessage();
            BeanUtil.copyProperties(chatMessage, newChatMessage);
            newChatMessage.setReceiverTerminals(entry.getValue());
            String topic = ImConstants.IM_CHAT_MESSAGE_TOPIC + "-" + environmentName;
            return new ImTopicInfo<>(newChatMessage, topic, entry.getKey());
        }).collect(Collectors.toList());
        if (topicMessages.isEmpty()) {
            if (log.isDebugEnabled()) {
                log.debug("没有在线终端，消息发送失败：{}", JSON.toJSONString(chatMessage));
            }
            return;
        }
        if (async) {
            messageSender.asyncBatchSend(topicMessages, null);
        } else {
            boolean send = messageSender.batchSend(topicMessages);
            if (!send) {
                throw new ImException(ImExceptionCode.MESSAGE_SENT_TO_MQ_FAIL);
            }
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
