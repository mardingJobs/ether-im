package cn.ether.im.sdk.mq;

import cn.ether.im.common.constants.ImConstants;
import cn.ether.im.common.model.message.ImMessageEvent;
import cn.ether.im.sdk.listener.MessageEventBroadcast;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
@RocketMQMessageListener(consumerGroup = ImConstants.IM_MESSAGE_EVENT_CONSUMER_GROUP,
        topic = ImConstants.IM_MESSAGE_EVENT_TOPIC)
public class MessageEventConsumer implements RocketMQListener<String> {

    @Resource
    private MessageEventBroadcast messageEventBroadcast;

    /**
     * @param message
     */
    @Override
    public void onMessage(String message) {
        if (StringUtils.isEmpty(message)) {
            return;
        }
        // todo 消息事件处理失败
        ImMessageEvent messageEvent = JSON.parseObject(message, ImMessageEvent.class);
        messageEventBroadcast.broadcast(messageEvent);
    }
}
