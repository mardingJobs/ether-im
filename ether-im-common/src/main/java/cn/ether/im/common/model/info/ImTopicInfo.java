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
package cn.ether.im.common.model.info;

import cn.ether.im.common.model.message.ImMessageV2;
import com.google.common.base.Objects;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImTopicInfo<T extends ImMessageV2> {

    private static final long serialVersionUID = -7962158433664656629L;

    /**
     * 发送到MQ的消息
     */
    private T message;

    /**
     * 消息主题
     */
    private String topic;

    /**
     * 消息标签
     */
    private String tag;

    public ImTopicInfo(T message, String topic) {
        this.message = message;
        this.topic = topic;
    }

    public String getDestination() {
        if (StringUtils.isEmpty(tag)) {
            return topic;
        }
        return topic + ":" + tag;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ImTopicInfo)) return false;
        ImTopicInfo<?> topicInfo = (ImTopicInfo<?>) o;
        return Objects.equal(message.messageId(), topicInfo.message.messageId())
                && Objects.equal(topic, topicInfo.topic)
                && Objects.equal(tag, topicInfo.tag);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(message.messageId(), topic, tag);
    }
}
