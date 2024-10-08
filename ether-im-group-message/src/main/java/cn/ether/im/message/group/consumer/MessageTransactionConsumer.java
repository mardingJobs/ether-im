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
package cn.ether.im.message.group.consumer;

import cn.ether.im.common.constants.ImConstants;
import cn.ether.im.common.enums.ImChatMessageStatus;
import cn.ether.im.common.enums.ImMessageSendResult;
import cn.ether.im.common.model.message.ImGroupMessage;
import cn.ether.im.message.group.model.entity.ImGroupMessageET;
import cn.ether.im.message.group.model.entity.ImGroupUserET;
import cn.ether.im.message.group.service.ImGroupMessageService;
import cn.ether.im.message.group.service.ImGroupUserETService;
import cn.ether.im.sdk.agent.ImMessageAgent;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQPushConsumerLifecycleListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RocketMQMessageListener(consumerGroup = "IM-GROUP-MESSAGE-TX-CONSUMERS",
        topic = ImConstants.IM_GROUP_TX_MESSAGE_TOPIC, consumeMode = ConsumeMode.CONCURRENTLY)
public class MessageTransactionConsumer
        implements RocketMQListener<ImGroupMessage>, RocketMQPushConsumerLifecycleListener {

    @Resource
    private ImMessageAgent messageAgent;

    @Resource
    private ImGroupMessageService messageService;

    @Resource
    private ImGroupUserETService userETService;

    @Override
    public void onMessage(ImGroupMessage message) {
        log.info("监听到【群事物消息】|{}", message);
        String groupId = message.getGroupId();
        List<ImGroupUserET> groupUserETList = userETService.lambdaQuery().eq(ImGroupUserET::getGroupId, groupId).list();

        List<String> groupUserIds = groupUserETList.stream().map(ImGroupUserET::getUserId).collect(Collectors.toList());
        message.setReceiverIds(groupUserIds);
        ImMessageSendResult imMessageSendResult = null;
        try {
            imMessageSendResult = messageAgent.sendGroupMessage(message);
            log.info("消息ID:{} 发送MQ结果：{},", message.getMessageId(), imMessageSendResult);
        } catch (Exception e) {
            log.error("消息发送异常：{}", message, e);
        }
        messageService.lambdaUpdate().eq(ImGroupMessageET::getMessageId, message.getMessageId())
                .eq(ImGroupMessageET::getStatus, ImChatMessageStatus.INIT.name())
                .set(ImGroupMessageET::getStatus, imMessageSendResult)
                .update();
    }

    @Override
    public void prepareStart(DefaultMQPushConsumer consumer) {
    }
}
