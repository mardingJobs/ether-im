package cn.ether.im.common.mq.rocket;

import cn.ether.im.common.model.message.ImMessage;
import cn.ether.im.common.model.message.ImTopicMessage;
import cn.ether.im.common.mq.ImMessageSender;
import com.alibaba.fastjson.JSON;
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
        String messageString = JSON.toJSONString(message.getMessage());
        log.info("发送MQ消息：{}", messageString);
        Message<String> msg = MessageBuilder.withPayload(messageString).build();
        SendResult sendResult = rocketMQTemplate.syncSend(message.getTopic(), msg);
        return SendStatus.SEND_OK.equals(sendResult.getSendStatus());
    }

    /**
     * 同步发送顺序消息到MQ
     *
     * @param message
     * @return
     * @throws Exception
     */
    @Override
    public boolean sendOrderlyByUniqueId(ImTopicMessage<? extends ImMessage> message) throws Exception {
        ImMessage msgEntity = message.getMessage();
        String messageString = JSON.toJSONString(msgEntity);
        log.info("发送顺序MQ消息：{}", messageString);
        Message<String> msg = MessageBuilder.withPayload(messageString).build();
        SendResult sendResult = rocketMQTemplate.syncSendOrderly(message.getTopic(), msg, msgEntity.getUniqueId());
        return SendStatus.SEND_OK.equals(sendResult.getSendStatus());
    }

    /**
     * 批量发送IM消息,所有消息的Topic必须相同,Tag 可以不同。
     *
     * @param messages
     * @return
     */

    @Override
    public boolean batchSend(List<ImTopicMessage> messages) throws Exception {
        List<org.apache.rocketmq.common.message.Message> msgs = messages.stream()
                .map((message) -> {
                    org.apache.rocketmq.common.message.Message msg = new org.apache.rocketmq.common.message.Message(message.getTopic(), message.getTag(), JSONObject.toJSONString(message.getMessage()).getBytes());
                    return msg;
                })
                .collect(Collectors.toList());
        SendResult sendResult = rocketMQTemplate.getProducer().send(msgs);
        return SendStatus.SEND_OK.equals(sendResult.getSendStatus());
    }
}
