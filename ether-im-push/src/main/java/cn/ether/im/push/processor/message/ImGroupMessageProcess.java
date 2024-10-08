package cn.ether.im.push.processor.message;

import cn.ether.im.client.common.enums.ImInfoType;
import cn.ether.im.common.cache.ImUserContextCache;
import cn.ether.im.common.cache.RemoteCacheService;
import cn.ether.im.common.constants.ImConstants;
import cn.ether.im.common.event.event.impl.ImGroupMessageReceivedEvent;
import cn.ether.im.common.exception.RetryException;
import cn.ether.im.common.model.message.ImGroupMessage;
import cn.ether.im.common.model.user.ImUserTerminal;
import cn.ether.im.common.util.ThreadPoolUtils;
import cn.ether.im.push.processor.ImInfoProcessor;
import cn.ether.im.push.processor.flusher.ImMessageFlusher;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * * @Author: Martin
 * * @Date    2024/9/14 23:22
 * * @Description
 **/
@Slf4j
@Component
public class ImGroupMessageProcess extends ImInfoProcessor<ImGroupMessage> {

    @Value("${server.id}")
    private String serverId;

    @Resource
    private RemoteCacheService remoteCacheService;
    @Resource
    private ImMessageFlusher messageFlusher;
    private ThreadPoolExecutor flushExecutor = ThreadPoolUtils.createExecutor(20, 2048);
    @Resource
    private RocketMQTemplate rocketMQTemplate;
    @Resource
    private ImUserContextCache userContextCache;


    private void cacheUnReceivedMessage(Long messageId, List<ImUserTerminal> receiverTerminals) {
        String[] values = receiverTerminals.stream().map(ImUserTerminal::toString).toArray(String[]::new);
        String key = ImConstants.UN_RECEIVED_MSG_PREFIX + messageId;
        remoteCacheService.addSet(key, values);
        remoteCacheService.expire(key, 5, TimeUnit.MINUTES);
    }

    @Override
    public void doProcess(ChannelHandlerContext ctx, ImGroupMessage message) {
        List<String> receiverIds = message.getReceiverIds();
        // 判断是当前服务处理的
        receiverIds = receiverIds.stream().filter(receiverId -> {
            String connServerId = userContextCache.connectedServerId(receiverId);
            return Objects.equals(connServerId, serverId);
        }).collect(Collectors.toList());

        for (String receiverId : receiverIds) {
            List<ImUserTerminal> limitTerminals = message.getLimitTerminals();
            List<ImUserTerminal> receiverTerminals = userContextCache.onlineTerminals(receiverId);
            if (CollectionUtil.isEmpty(receiverTerminals)) {
                log.info("用户{}不在线", receiverId);
                return;
            }
            // 过滤
            if (CollectionUtil.isNotEmpty(limitTerminals)) {
                receiverTerminals = receiverTerminals.stream().filter(limitTerminals::contains).collect(Collectors.toList());
            }
            cacheUnReceivedMessage(message.getMessageId(), receiverTerminals);
            for (ImUserTerminal terminal : receiverTerminals) {
                flushExecutor.execute(() -> {
                    try {
                        boolean received = messageFlusher.flush(terminal, message);
                        if (received) {
                            // 发布MQ消息
                            ImGroupMessageReceivedEvent messageReceivedEvent = new ImGroupMessageReceivedEvent(message.messageId(), terminal.getUserId());
                            byte[] bytes = JSON.toJSONString(messageReceivedEvent).getBytes(StandardCharsets.UTF_8);
                            Message msg = new Message(ImConstants.IM_GROUP_MESSAGE_RECEIVED_EVENT_TOPIC, bytes);
                            SendResult sendResult = rocketMQTemplate.getProducer().send(msg);
                            log.info("消息触达事件发送MQ结果:{},MessageId:{},UserTerminal:{}", sendResult.getSendStatus(), message.messageId(), terminal);
                        }
                    } catch (RetryException e) {
                        log.warn("重复推送消息后未收到触达消息，MessageId:{},UserTerminal:{}", message.messageId(), terminal);
                    } catch (Exception e) {
                        log.error("消息推送失败,MessageId:{}", message.messageId(), e);
                    }
                });
            }
        }
    }

    @Override
    public ImInfoType supportType() {
        return ImInfoType.GROUP;
    }
}
