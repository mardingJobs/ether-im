package cn.ether.im.sdk.mq;

import cn.ether.im.common.constants.ImConstants;
import cn.ether.im.common.model.message.ImMessageSendResult;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RocketMQMessageListener(consumerGroup = ImConstants.IM_RESULT_PERSONAL_CONSUMER_GROUP,
        topic = ImConstants.TOPIC_IM_RESULT)
public class PersonalMessageResultConsumer implements RocketMQListener<String> {


    /**
     * @param message
     */
    @Override
    public void onMessage(String message) {

        if (StringUtils.isEmpty(message)) {
            return;
        }
        ImMessageSendResult result = JSON.parseObject(message, ImMessageSendResult.class);

    }
}
