package cn.ether.im.sdk.mq;

import cn.ether.im.common.constants.ImConstants;
import cn.ether.im.common.model.message.ImMessageEvent;
import cn.ether.im.sdk.listener.MessageEventBroadcast;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
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
public class MessageEventConsumer implements RocketMQListener<String>, RocketMQPushConsumerLifecycleListener {

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
        ImMessageEvent messageEvent = JSON.parseObject(message, ImMessageEvent.class);
        messageEventBroadcast.broadcast(messageEvent);
    }

    @Override
    public void prepareStart(DefaultMQPushConsumer consumer) {
        int cpuNums = Runtime.getRuntime().availableProcessors();
        consumer.setConsumeThreadMin(cpuNums);
        consumer.setConsumeThreadMax(cpuNums * 2);
        // todo 设置批量消费和消费幂等。
//        consumer.setPullBatchSize();
        // 消费失败时重试
        consumer.setMaxReconsumeTimes(3);
    }
}
