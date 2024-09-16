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
import cn.ether.im.common.model.message.ImChatMessage;
import cn.ether.im.push.processor.MessageProcessor;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQPushConsumerLifecycleListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(name = "message.mq.type", havingValue = "rocketmq", matchIfMissing = true)
@RocketMQMessageListener(consumerGroup = ImConstants.IM_MESSAGE_PUSH_CONSUMERS,
        topic = ImConstants.IM_MESSAGE_PUSH_TOPIC)
public class ChatMessageConsumer
        implements RocketMQListener<String>, RocketMQPushConsumerLifecycleListener {

    @Value("${server.id}")
    private Long serverId;

    @Value("${server.group}")
    private String serverGroup;

    @Override
    public void onMessage(String message) {
        if (StrUtil.isEmpty(message)) {
            log.warn("PersonalMessageConsumer.onMessage|接收到的消息为空");
            return;
        }
        ImChatMessage chatMessage = JSON.parseObject(message, ImChatMessage.class);
        if (chatMessage == null) {
            log.warn("onMessage|转化后的数据为空");
            return;
        }
        MessageProcessor.processChatMessage(chatMessage);
    }

    @Override
    public void prepareStart(DefaultMQPushConsumer consumer) {
        try {
            String group = StringUtils.isEmpty(serverGroup) ? ImConstants.DEFAULT_GROUP_NAME : serverGroup;
            String topic = String.join(ImConstants.MQ_MESSAGE_KEY_SPLIT, group,
                    ImConstants.IM_MESSAGE_PUSH_TOPIC, String.valueOf(serverId));
            consumer.subscribe(topic, "*");
        } catch (Exception e) {
            log.error("prepareStart|异常:{}", e.getMessage());
        }
    }
}
