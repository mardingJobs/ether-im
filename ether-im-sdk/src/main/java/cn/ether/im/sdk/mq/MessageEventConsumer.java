package cn.ether.im.sdk.mq;

import cn.ether.im.common.constants.ImConstants;
import cn.ether.im.common.model.message.ImMessageEvent;
import cn.ether.im.sdk.listener.MessageEventBroadcast;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQPushConsumerLifecycleListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
@RocketMQMessageListener(consumerGroup = ImConstants.IM_MESSAGE_EVENT_CONSUMER_GROUP,
        topic = ImConstants.IM_MESSAGE_EVENT_TOPIC, consumeMode = ConsumeMode.CONCURRENTLY)
public class MessageEventConsumer implements RocketMQListener<MessageExt>, RocketMQPushConsumerLifecycleListener {

    @Resource
    private MessageEventBroadcast messageEventBroadcast;

    /**
     * @param messageExt
     */
    @Override
    public void onMessage(MessageExt messageExt) {
        String body = new String(messageExt.getBody());
        if (StringUtils.isEmpty(body)) {
            return;
        }
        ImMessageEvent messageEvent = JSON.parseObject(body, ImMessageEvent.class);
        messageEventBroadcast.broadcast(messageEvent);
    }

    /**
     * @param consumer
     */
    @Override
    public void prepareStart(DefaultMQPushConsumer consumer) {

    }
}
