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
import cn.ether.im.common.event.event.ImUserLoginEvent;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQPushConsumerLifecycleListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RocketMQMessageListener(consumerGroup = "IM_SDK_",
        topic = ImConstants.IM_LOGIN_EVENT_TOPIC, consumeMode = ConsumeMode.CONCURRENTLY)
public class UserLoginEventConsumer
        implements RocketMQListener<String>, RocketMQPushConsumerLifecycleListener {

    @Override
    public void onMessage(String message) {
        log.info("监听到用户登陆事件消息|{}", message);
        if (StrUtil.isEmpty(message)) {
            return;
        }
        ImUserLoginEvent userLoginEvent = JSON.parseObject(message, ImUserLoginEvent.class);
        // todo 发送未读消息列表
    }

    @Override
    public void prepareStart(DefaultMQPushConsumer consumer) {
    }
}
