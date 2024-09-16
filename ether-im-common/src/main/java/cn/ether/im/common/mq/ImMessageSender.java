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
package cn.ether.im.common.mq;
import cn.ether.im.common.model.message.ImTopicMessage;

import java.util.List;

/**
 * IM Message 发送器
 */
public interface ImMessageSender {

    /**
     * 发送IM消息到MQ
     * @param message 发送的消息
     */
    boolean send(ImTopicMessage message);

    /**
     * 批量发送IM消息
     *
     * @param messages
     * @return
     */
    boolean batchSend(List<ImTopicMessage> messages);

}
