package cn.ether.im.message.listener;

import cn.ether.im.common.model.message.ImMessageEvent;
import cn.ether.im.message.service.MessageService;
import cn.ether.im.sdk.listener.ImMessageEventListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * * @Author: Martin
 * * @Date    2024/9/17 18:53
 * * @Description
 **/
@Slf4j
@Component
public class MessageEventListener implements ImMessageEventListener {

    @Resource
    private MessageService messageService;

    /**
     * @param messageEvent
     */
    @Override
    public void onMessageEvent(ImMessageEvent messageEvent) {
        messageService.onMessageEvent(messageEvent);
    }
}
