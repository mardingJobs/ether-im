package cn.ether.im.message.listener;

import cn.ether.im.common.event.listener.ImMessageEventListener;
import cn.ether.im.common.model.info.message.event.ImMessageEvent;
import cn.ether.im.message.service.ChatMessageService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;

/** 消息状态持久化
 * * @Author: Martin
 * * @Date    2024/9/17 18:53
 * * @Description
 **/
@Slf4j
//@ImEventListener(listenEventTypes = {SENT, PUSHED, REACHED})
@Deprecated
public class DefaultMessageEventListener implements ImMessageEventListener {

    @Resource
    private ChatMessageService messageService;

    /**
     * @param messageEvent
     */
    @Override
    public void onMessageEvent(ImMessageEvent messageEvent) throws Exception {
        log.info("监听到消息事件:{}", JSON.toJSONString(messageEvent));
        //messageService.onMessageEventV2(messageEvent);
    }
}
