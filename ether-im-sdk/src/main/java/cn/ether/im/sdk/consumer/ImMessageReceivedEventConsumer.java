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
package cn.ether.im.sdk.consumer;

import cn.ether.im.common.constants.ImConstants;
import cn.ether.im.common.event.broadcast.ImEventAsyncBroadcast;
import cn.ether.im.common.event.event.impl.ImMessageReceivedEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQPushConsumerLifecycleListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
@RocketMQMessageListener(consumerGroup = "IM-MESSAGE-RECEIVED-EVENT-GROUP",
        topic = ImConstants.IM_MESSAGE_RECEIVED_EVENT_TOPIC, consumeMode = ConsumeMode.CONCURRENTLY)
public class ImMessageReceivedEventConsumer
        implements RocketMQListener<ImMessageReceivedEvent>, RocketMQPushConsumerLifecycleListener {

    @Resource
    private ImEventAsyncBroadcast<ImMessageReceivedEvent> eventBroadcast;

    @Override
    public void onMessage(ImMessageReceivedEvent receivedEvent) {
        log.info("消费到【消息已接收事件】|{}", receivedEvent);
        eventBroadcast.broadcast(receivedEvent);
    }

    @Override
    public void prepareStart(DefaultMQPushConsumer consumer) {

    }
}
