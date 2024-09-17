package cn.ether.im.sdk.listener;

import cn.ether.im.common.model.message.ImMessageEvent;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * * @Author: Martin
 * * @Date    2024/9/15 12:06
 * * @Description
 **/
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
            listener.onMessageEvent(messageEvent);
        }

    }
}
