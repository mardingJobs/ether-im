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
package cn.ether.im.message.single.consumer;

import cn.ether.im.common.constants.ImConstants;
import cn.ether.im.common.enums.ImMessageSendResult;
import cn.ether.im.common.model.message.ImSingleMessage;
import cn.ether.im.sdk.client.EtherImClient;
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
@RocketMQMessageListener(consumerGroup = "IM-SINGLE-MESSAGE-TX-CONSUMERS",
        topic = ImConstants.IM_CHAT_TX_MESSAGE_TOPIC, consumeMode = ConsumeMode.CONCURRENTLY)
public class SingleMessageTransactionConsumer
        implements RocketMQListener<ImSingleMessage>, RocketMQPushConsumerLifecycleListener {

    @Resource
    private EtherImClient etherImClient;

    @Override
    public void onMessage(ImSingleMessage message) {
        log.info("监听到【单聊事物消息】|{}", message);
        ImMessageSendResult imMessageSendResult = etherImClient.sendSingleMessage(message);
        log.info("消息发送结果：{}", imMessageSendResult);
    }

    @Override
    public void prepareStart(DefaultMQPushConsumer consumer) {
    }
}
