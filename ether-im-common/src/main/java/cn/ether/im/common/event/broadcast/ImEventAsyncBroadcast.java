package cn.ether.im.common.event.broadcast;

import cn.ether.im.common.event.event.ImEvent;
import cn.ether.im.common.event.event.ImEventType;
import cn.ether.im.common.event.listener.ImEventListener;
import cn.ether.im.common.util.ThreadPoolUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 事件广播器
 * * @Author: Martin
 * * @Date    2024/9/15 12:06
 * * @Description
 **/
@Slf4j
@Component
public class ImEventAsyncBroadcast<T extends ImEvent> implements ImEventBroadcast<T> {

    private static final ThreadPoolExecutor executor = ThreadPoolUtils.createExecutor(16, 2048);
    @Autowired(required = false)
    private List<ImEventListener<? extends ImEvent>> imEventListeners;

    @Override
    public void broadcast(T event) {
        if (CollectionUtils.isEmpty(imEventListeners)) {
            return;
        }
        for (ImEventListener listener : imEventListeners) {
            List<ImEventType> eventTypes = listener.listenEventType();
            if (CollectionUtils.isEmpty(eventTypes) || !eventTypes.contains(event.getEventType())) {
                continue;
            }
            executor.execute(() -> {
                try {
                    listener.onEvent(event);
                } catch (Exception e) {
                    log.error("消息事件处理异常,MessageEvent:{}", event, e);
                }
            });
        }

    }
}
