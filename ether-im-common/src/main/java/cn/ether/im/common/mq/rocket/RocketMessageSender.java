package cn.ether.im.common.mq.rocket;

import cn.ether.im.common.model.message.ImTopicMessage;
import cn.ether.im.common.mq.ImMessageSender;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

/**
 * * @Author: Martin
 * * @Date    2024/9/14 19:10
 * * @Description
 **/
@Slf4j
@Component
@ConditionalOnProperty(name = "message.mq.type", havingValue = "rocketmq", matchIfMissing = true)
public class RocketMessageSender implements ImMessageSender {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 发送IM消息到MQ
     *
     * @param message 发送的消息
     */
    @Override
    public boolean send(ImTopicMessage message) {
        try {
            Message<String> msg = MessageBuilder.withPayload(JSONObject.toJSONString(message.getMessage())).build();
            SendResult sendResult = rocketMQTemplate.syncSend(message.getTopic(), msg);
            return SendStatus.SEND_OK.equals(sendResult.getSendStatus());
        } catch (Exception e) {
            log.error("send message to mq error", e);
            return false;
        }
    }
}
