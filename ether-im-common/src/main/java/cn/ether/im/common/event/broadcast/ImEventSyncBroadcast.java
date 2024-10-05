package cn.ether.im.common.event.broadcast;

import cn.ether.im.common.event.event.ImEvent;
import cn.ether.im.common.event.event.ImEventType;
import cn.ether.im.common.event.listener.ImEventListener;
import cn.ether.im.common.exception.ImEventListenException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 事件广播器,同步调用监听器
 * * @Author: Martin
 * * @Date    2024/9/15 12:06
 * * @Description
 **/
@Slf4j
@Component
public class ImEventSyncBroadcast<T extends ImEvent> implements ImEventBroadcast<T> {

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
            try {
                listener.onEvent(event);
            } catch (Exception e) {
                throw new ImEventListenException(e);
            }
        }

    }
}
