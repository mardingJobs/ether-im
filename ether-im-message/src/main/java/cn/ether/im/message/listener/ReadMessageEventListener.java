package cn.ether.im.message.listener;

import cn.ether.im.common.enums.ImChatMessageType;
import cn.ether.im.common.event.ImMessageEventType;
import cn.ether.im.common.event.listener.ImEventListener;
import cn.ether.im.common.event.listener.ImMessageEventListener;
import cn.ether.im.common.model.message.ImChatMessage;
import cn.ether.im.common.model.message.ImMessageEvent;
import cn.ether.im.common.model.user.ImUser;
import cn.ether.im.message.model.entity.ImChatMessageEntity;
import cn.ether.im.message.service.ChatMessageService;
import cn.ether.im.message.service.ImChatMessageService;
import cn.ether.im.sdk.client.EtherImClient;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.Collections;

/**
 * * @Author: Martin(微信：martin-jobs)
 * * @Date    2024/9/19 14:21
 * * @Description
 * * @Github <a href="https://github.com/mardingJobs">Github链接</a>
 **/
@Slf4j
@ImEventListener(listenEventTypes = ImMessageEventType.READ)
public class ReadMessageEventListener implements ImMessageEventListener {

    @Resource
    private ChatMessageService messageService;

    @Resource
    private ImChatMessageService personalMessageService;

    @Resource
    private EtherImClient imClient;

    /**
     * @param messageEvent
     * @throws Exception
     */
    @Override
    public void onMessageEvent(ImMessageEvent messageEvent) throws Exception {
        log.info("监听到消息事件:{}", JSON.toJSONString(messageEvent));
        messageService.onMessageEventV2(messageEvent);
        Long messageId = messageEvent.getMessageId();
        ImChatMessageEntity messageEntity = personalMessageService.getById(messageId);
        if (messageEntity == null) {
            return;
        }
        ImUser receiver = new ImUser(messageEntity.getSenderId());
        ImChatMessage chatMessage = new ImChatMessage();
        chatMessage.setId(messageId);
        chatMessage.setSender(messageEvent.getTerminal());
        chatMessage.setReceivers(Collections.singletonList(receiver));
        chatMessage.setChatMessageType(ImChatMessageType.READ);
        chatMessage.setSendTime(messageEvent.getEventTime());
        imClient.sendChatMessage(chatMessage);
    }
}
