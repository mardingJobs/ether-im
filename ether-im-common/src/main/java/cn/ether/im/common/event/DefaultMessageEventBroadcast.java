package cn.ether.im.common.event;

import cn.ether.im.common.event.listener.ImEventListener;
import cn.ether.im.common.event.listener.ImMessageEventListener;
import cn.ether.im.common.model.message.ImMessageEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 *
 * 事件广播器
 * * @Author: Martin
 * * @Date    2024/9/15 12:06
 * * @Description
 **/
@Slf4j
@Component
public class DefaultMessageEventBroadcast implements MessageEventBroadcast {

    @Autowired(required = false)
    private List<ImMessageEventListener> imMessageEventListeners;


    @Override
    public void broadcast(ImMessageEvent messageEvent) {
        if (CollectionUtils.isEmpty(imMessageEventListeners)) {
            return;
        }
        for (ImMessageEventListener listener : imMessageEventListeners) {
            try {
                ImEventListener annotation = listener.getClass().getAnnotation(ImEventListener.class);
                if (annotation == null) {
                    annotation = listener.getClass().getSuperclass().getAnnotation(ImEventListener.class);
                    if (annotation == null) {
                        continue;
                    }
                }
                ImMessageEventType[] imMessageEventTypes = annotation.listenEventTypes();
                boolean listen = imMessageEventTypes == null || imMessageEventTypes.length == 0
                        || Arrays.asList(imMessageEventTypes).contains(messageEvent.getEventType());
                if (listen) listener.onMessageEvent(messageEvent);
            } catch (Exception e) {
                log.error("消息事件处理异常,MessageEvent:{}", messageEvent, e);
            }
        }

    }
}
