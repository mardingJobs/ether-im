package cn.ether.im.message.listener;

import cn.ether.im.common.model.message.ImMessageEvent;
import cn.ether.im.message.service.MessageService;
import cn.ether.im.sdk.listener.ImMessageEventListener;
import com.alibaba.fastjson.JSON;
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
public class DefaultMessageEventListener implements ImMessageEventListener {

    @Resource
    private MessageService messageService;

    /**
     * @param messageEvent
     */
    @Override
    public void onMessageEvent(ImMessageEvent messageEvent) throws Exception {
        log.info("监听到消息事件:{}", JSON.toJSONString(messageEvent));
        messageService.onMessageEventV2(messageEvent);
    }
}
