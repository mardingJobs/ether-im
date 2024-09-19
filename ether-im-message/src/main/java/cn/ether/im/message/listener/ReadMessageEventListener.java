package cn.ether.im.message.listener;

import cn.ether.im.common.enums.ImChatMessageType;
import cn.ether.im.common.enums.ImMessageEventType;
import cn.ether.im.common.event.ImEventListener;
import cn.ether.im.common.model.message.ImChatMessage;
import cn.ether.im.common.model.message.ImMessageEvent;
import cn.ether.im.common.model.user.ImUser;
import cn.ether.im.message.entity.ImPersonalMessageEntity;
import cn.ether.im.message.service.ImPersonalMessageService;
import cn.ether.im.message.service.MessageService;
import cn.ether.im.sdk.client.EtherImClient;
import cn.ether.im.sdk.listener.ImMessageEventListener;
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
    private MessageService messageService;

    @Resource
    private ImPersonalMessageService personalMessageService;

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
        ImPersonalMessageEntity messageEntity = personalMessageService.getById(messageId);
        if (messageEntity == null) {
            return;
        }
        ImUser receiver = new ImUser(messageEntity.getSenderId(), messageEntity.getSenderGroup());
        ImChatMessage chatMessage = new ImChatMessage();
        chatMessage.setId(messageId);
        chatMessage.setSender(messageEvent.getTerminal());
        chatMessage.setReceivers(Collections.singletonList(receiver));
        chatMessage.setType(ImChatMessageType.READ);
        chatMessage.setSendTime(messageEvent.getEventTime());
        imClient.sendChatMessage(chatMessage);
    }
}
