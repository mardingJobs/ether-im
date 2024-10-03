package cn.ether.im.push.processor.message;

import cn.ether.im.common.cache.RemoteCacheService;
import cn.ether.im.common.constants.ImConstants;
import cn.ether.im.common.enums.ImInfoType;
import cn.ether.im.common.event.event.ImMessageReceivedEvent;
import cn.ether.im.common.exception.RetryException;
import cn.ether.im.common.model.info.message.ImMessage;
import cn.ether.im.common.model.user.ImUserTerminal;
import cn.ether.im.common.util.ThreadPoolUtils;
import cn.ether.im.push.processor.ImInfoProcessor;
import cn.ether.im.push.processor.flusher.ImMessageFlusher;
import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.List;
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
public class ImMessageProcess extends ImInfoProcessor<ImMessage> {

    @Resource
    private RemoteCacheService remoteCacheService;

    @Resource
    private ImMessageFlusher messageFlusher;

    private ThreadPoolExecutor flushExecutor = ThreadPoolUtils.createExecutor(20, 2048);
    @Autowired
    private RocketMQTemplate rocketMQTemplate;


    private void cacheUnReceivedMessage(Long messageId, List<ImUserTerminal> receiverTerminals) {
        List<String> list = receiverTerminals.stream().map(ImUserTerminal::toString).collect(Collectors.toList());
        String[] values = list.toArray(new String[0]);
        String key = ImConstants.UN_RECEIVED_MSG_PREFIX + messageId;
        remoteCacheService.addSet(key, values);
        remoteCacheService.expire(key, 5, TimeUnit.MINUTES);
    }

    @Override
    public void doProcess(ChannelHandlerContext ctx, ImMessage message) {
        List<ImUserTerminal> receiverTerminals = message.getReceiverTerminals();
        cacheUnReceivedMessage(message.getId(), receiverTerminals);
        for (ImUserTerminal terminal : receiverTerminals) {
            flushExecutor.execute(() -> {
                try {
                    boolean received = messageFlusher.flush(terminal, message);
                    if (received) {
                        // 发布MQ消息
                        ImMessageReceivedEvent messageReceivedEvent = new ImMessageReceivedEvent(message.getId(), terminal.getUserId());
                        byte[] bytes = JSON.toJSONString(messageReceivedEvent).getBytes(StandardCharsets.UTF_8);
                        Message msg = new Message(ImConstants.IM_MESSAGE_RECEIVED_EVENT_TOPIC, bytes);
                        SendResult sendResult = rocketMQTemplate.getProducer().send(msg);
                        log.info("消息触达事件发送MQ结果:{},MessageId:{},UserTerminal:{}", sendResult.getSendStatus(), message.getId(), terminal);
                    }
                } catch (RetryException e) {
                    log.warn("重复推送消息后未收到触达消息，MessageId:{},UserTerminal:{}", message.getId(), terminal);
                } catch (Exception e) {
                    log.error("消息推送失败,MessageId:{}", message.getId(), e);
                }
            });
        }

    }

    @Override
    public ImInfoType supportType() {
        return ImInfoType.MESSAGE;
    }
}
