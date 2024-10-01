package cn.ether.im.common.mq.rocket;

import cn.ether.im.common.enums.ImExceptionCode;
import cn.ether.im.common.exception.ImException;
import cn.ether.im.common.model.info.ImInfo;
import cn.ether.im.common.model.info.ImTopicInfo;
import cn.ether.im.common.model.info.message.ImMessage;
import cn.ether.im.common.mq.ImMqMessageSender;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.client.producer.selector.SelectMessageQueueByHash;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
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
public class RocketMessageSender implements ImMqMessageSender {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;


    private static Message createMqMessage(ImTopicInfo topicMessage, String messageString) {
        Message message = new Message(topicMessage.getTopic(), topicMessage.getTag(), messageString.getBytes(StandardCharsets.UTF_8));
        message.setKeys(String.valueOf(topicMessage.getMessage().getId()));
        return message;
    }

    /**
     * 同步发送IM消息到MQ
     *
     * @param topicMessage 发送的消息
     */
    @Override
    public boolean send(ImTopicInfo topicMessage) throws Exception {
        String messageString = JSON.toJSONString(topicMessage.getMessage());
        if (log.isDebugEnabled()) {
            log.debug("同步发送MQ消息：{}", messageString);
        }
        Message message = createMqMessage(topicMessage, messageString);
        SendResult sendResult = rocketMQTemplate.getProducer().send(message);
        return SendStatus.SEND_OK.equals(sendResult.getSendStatus());
    }


    @Override
    public void asyncSend(ImTopicInfo topicMessage, SendCallback callback) {
        String messageString = JSON.toJSONString(topicMessage.getMessage());
        if (log.isDebugEnabled()) {
            log.debug("异步发送MQ消息：{}", messageString);
        }
        Message message = createMqMessage(topicMessage, messageString);
        try {
            rocketMQTemplate.getProducer().send(message, callback);
        } catch (MQClientException | RemotingException | InterruptedException e) {
            log.error("发送MQ消息失败", e);
            throw new ImException(ImExceptionCode.MESSAGE_SENT_TO_MQ_FAIL);
        }
    }

    /**
     * 同步发送顺序消息到MQ
     *
     * @param topicMessage
     * @return
     * @throws Exception
     */
    @Override
    public boolean sendOrderlyByUid(ImTopicInfo<? extends ImInfo> topicMessage) throws Exception {
        ImMessage imMessage = topicMessage.getMessage();
        String messageString = JSON.toJSONString(imMessage);
        if (log.isDebugEnabled()) {
            log.info("发送顺序MQ消息：{}", messageString);
        }
        Message message = createMqMessage(topicMessage, messageString);
        SendResult sendResult = rocketMQTemplate.getProducer().send(message, new SelectMessageQueueByHash(), imMessage.getId());
        return SendStatus.SEND_OK.equals(sendResult.getSendStatus());
    }

    /**
     * 批量发送IM消息,所有消息的Topic必须相同,Tag 可以不同。
     *
     * @param messages
     * @return
     */

    @Override
    public boolean batchSend(List<ImTopicInfo> messages) throws Exception {
        List<Message> msgs = messages.stream()
                .map((message) -> {
                    Message msg = new Message(message.getTopic(), message.getTag(), JSONObject.toJSONString(message.getMessage()).getBytes());
                    return msg;
                })
                .collect(Collectors.toList());
        SendResult sendResult = rocketMQTemplate.getProducer().send(msgs);
        return SendStatus.SEND_OK.equals(sendResult.getSendStatus());
    }

    @Override
    public void asyncBatchSend(List<ImTopicInfo> messages, SendCallback callback) throws Exception {
        List<Message> msgs = messages.stream()
                .map((message) -> {
                    Message msg = new Message(message.getTopic(), message.getTag(), JSONObject.toJSONString(message.getMessage()).getBytes());
                    return msg;
                })
                .collect(Collectors.toList());
        rocketMQTemplate.getProducer().send(msgs, callback);
    }

    /**
     * @param topicMessage
     * @param arg
     * @return
     */
    @Override
    public TransactionSendResult sendMessageInTransaction(ImTopicInfo topicMessage, Object arg) {
        String messageString = JSON.toJSONString(topicMessage.getMessage());
        if (log.isDebugEnabled()) {
            log.debug("发送事务消息：{}", messageString);
        }
        Message message = createMqMessage(topicMessage, messageString);
        TransactionSendResult transactionSendResult = null;
        try {
            transactionSendResult = rocketMQTemplate.getProducer().sendMessageInTransaction(message, arg);
        } catch (MQClientException e) {
            log.error("发送事务消息失败", e);
            throw new ImException(ImExceptionCode.MESSAGE_SENT_TO_MQ_FAIL);
        }
        return transactionSendResult;


    }
}
