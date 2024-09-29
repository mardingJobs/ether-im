/**
 * Copyright 2022-9999 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.ether.im.push.mq;

import cn.ether.im.common.constants.ImConstants;
import cn.ether.im.common.model.info.message.ImMessage;
import cn.ether.im.common.util.ThreadPoolUtils;
import cn.ether.im.push.processor.InfoProcessor;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQPushConsumerLifecycleListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import static cn.ether.im.common.constants.ImConstants.IM_CHAT_MESSAGE_TOPIC;
import static cn.ether.im.common.constants.ImConstants.IM_MESSAGE_PUSH_CONSUMER_GROUP;

@Slf4j
@Component
@ConditionalOnProperty(name = "message.mq.type", havingValue = "rocketmq", matchIfMissing = true)
@RocketMQMessageListener(consumerGroup = IM_MESSAGE_PUSH_CONSUMER_GROUP,
        topic = IM_CHAT_MESSAGE_TOPIC, consumeMode = ConsumeMode.CONCURRENTLY)
public class ImChatMessageMQListener
        implements RocketMQListener<String>, RocketMQPushConsumerLifecycleListener {

    @Value("${server.id}")
    private Long serverId;

    @Value("${spring.profiles.active:default}")
    private String environmentName;

    @Override
    public void onMessage(String message) {
        log.info("收到MQ消息|{}", message);
        if (StrUtil.isEmpty(message)) {
            return;
        }
        ImMessage chatMessage = JSON.parseObject(message, ImMessage.class);
        if (chatMessage == null) {
            log.warn("onMessage|转化后的数据为空");
            return;
        }
        ThreadPoolUtils.execute(() -> {
            InfoProcessor.processChatMessage(chatMessage);
        });
    }

    @Override
    public void prepareStart(DefaultMQPushConsumer consumer) {
        try {
            String tag = ImConstants.IM_CHAT_MESSAGE_TAG_PREFIX + ImConstants.MQ_TOPIC_SPLIT + serverId;

            consumer.subscribe(IM_CHAT_MESSAGE_TOPIC + "-" + environmentName, tag);
            int cpuNums = Runtime.getRuntime().availableProcessors();
            consumer.setConsumeThreadMin(cpuNums);
            consumer.setConsumeThreadMax(cpuNums * 2);
            // 消费失败时重试
            consumer.setMaxReconsumeTimes(3);
            //  每个消费者的消费者分组都是不一样的
            consumer.setConsumerGroup(consumer.getConsumerGroup() + ImConstants.MQ_TOPIC_SPLIT + serverId);
        } catch (Exception e) {
            log.error("prepareStart|异常:{}", e.getMessage());
        }
    }
}
