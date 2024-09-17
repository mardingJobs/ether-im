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

import java.util.List;
import java.util.stream.Collectors;

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
    public boolean send(ImTopicMessage message) throws Exception {
        Message<String> msg = MessageBuilder.withPayload(JSONObject.toJSONString(message.getMessage())).build();
        SendResult sendResult = rocketMQTemplate.syncSend(message.getTopic(), msg);
        return SendStatus.SEND_OK.equals(sendResult.getSendStatus());
    }

    /**
     * 批量发送IM消息,所有消息的Topic必须相同
     *
     * @param messages
     * @return
     */
    @Override
    public boolean batchSend(List<ImTopicMessage> messages) throws Exception {
        List<Message<String>> messageList = messages.stream()
                .map((message) -> MessageBuilder.withPayload(JSONObject.toJSONString(message.getMessage())).build())
                .collect(Collectors.toList());
        SendResult sendResult = rocketMQTemplate.syncSend(messages.get(0).getTopic(), messageList);
        return SendStatus.SEND_OK.equals(sendResult.getSendStatus());
    }
}
