package cn.ether.im.sdk.sender.impl;

import cn.ether.im.common.cache.ImUserContextCache;
import cn.ether.im.common.constants.ImConstants;
import cn.ether.im.common.enums.ImMessageSendResult;
import cn.ether.im.common.model.info.ImTopicInfo;
import cn.ether.im.common.model.message.ImMessage;
import cn.ether.im.common.model.message.ImSingleMessage;
import cn.ether.im.common.model.user.ImUser;
import cn.ether.im.common.mq.ImMqMessageSender;
import cn.ether.im.sdk.sender.ImMessageSender;
import lombok.extern.slf4j.Slf4j;
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
public class DefaultImMessageSender implements ImMessageSender {

    @Resource
    private ImUserContextCache userContextCache;

    @Resource
    private ImMqMessageSender messageSender;


    @Override
    public void sendMessage(ImMessage message, boolean async) throws Exception {

    }

    @Override
    public ImMessageSendResult sendSingleMessage(ImSingleMessage singleMessage) {
        ImTopicInfo<ImSingleMessage> topicInfo = new ImTopicInfo<>();
        topicInfo.setTopic(ImConstants.IM_SINGLE_MESSAGE_TOPIC);
        List<String> serverIds = userContextCache.connectedServerIds(new ImUser(singleMessage.getReceiverId()));
        if (serverIds.isEmpty()) {
            return ImMessageSendResult.RECEIVER_NOT_ONLINE;
        }
        topicInfo.setTag(ImConstants.IM_CHAT_MESSAGE_TAG_PREFIX + serverIds.get(0));
        topicInfo.setMessage(singleMessage);
        boolean ok = false;
        try {
            ok = messageSender.send(topicInfo);
            if (!ok) {
                return ImMessageSendResult.FAIL;
            }
        } catch (Exception e) {
            return ImMessageSendResult.FAIL;
        }

        return ImMessageSendResult.SUCCESS;
    }

}
