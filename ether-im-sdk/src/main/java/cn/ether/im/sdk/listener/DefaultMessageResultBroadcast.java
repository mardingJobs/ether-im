package cn.ether.im.sdk.listener;

import cn.ether.im.common.model.message.ImMessageSendResult;
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
public class DefaultMessageResultBroadcast implements MessageResultBroadcast {

    @Autowired(required = false)
    private List<MessageResultListener> messageResultListeners;


    /**
     * @param result
     */
    @Override
    public void broadcast(ImMessageSendResult result) {
        if (CollectionUtils.isEmpty(messageResultListeners)) {
            return;
        }
        for (MessageResultListener listener : messageResultListeners) {
            listener.onMessageResult(result);
        }

    }
}
