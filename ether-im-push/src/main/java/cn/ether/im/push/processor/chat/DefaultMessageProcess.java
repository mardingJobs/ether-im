package cn.ether.im.push.processor.chat;

import cn.ether.im.common.event.ImMessageEventType;
import cn.ether.im.common.exception.RetryException;
import cn.ether.im.common.model.message.ImChatMessage;
import cn.ether.im.common.model.message.ImMessageEvent;
import cn.ether.im.common.model.user.ImUserTerminal;
import cn.ether.im.common.util.ThreadPoolUtils;
import cn.ether.im.push.mq.ImMessageEventProducer;
import cn.ether.im.push.processor.flusher.ImMessageFlusher;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * * @Author: Martin
 * * @Date    2024/9/14 23:22
 * * @Description
 **/
@Slf4j
@Component
public class DefaultMessageProcess implements ChatMessageProcess {

    @Resource
    private ImMessageEventProducer eventProducer;

    @Resource
    private ImMessageFlusher messageFlusher;

    private ThreadPoolExecutor flushExecutor = ThreadPoolUtils.createExecutor(10, 1024);


    @Override
    public void process(ImChatMessage message) {
        List<ImUserTerminal> receiverTerminals = message.getReceiverTerminals();

        for (ImUserTerminal terminal : receiverTerminals) {
            try {
                flushExecutor.execute(() -> {
                    messageFlusher.flush(terminal, message);
                });
            } catch (RetryException e) {
                log.warn("重复推送消息后未收到触达消息，MessageId:{},UserTerminal:{}", message.getId(), JSON.toJSONString(terminal));
            } catch (Exception e) {
                log.error("消息推送失败,MessageId:{}", message.getId(), e);
            }
        }
    }

    private void publishMessageEvent(ImChatMessage message, ImUserTerminal terminal) {
        try {
            // 发送消息已推送事件
            ImMessageEvent messageEvent = new ImMessageEvent();
            messageEvent.setMessageId(message.getId());
            messageEvent.setEventType(ImMessageEventType.PUSHED);
            messageEvent.setTerminal(terminal);
            messageEvent.setChatMessageType(message.getChatMessageType());
            messageEvent.setEventTime(System.currentTimeMillis());
            eventProducer.publish(messageEvent);
        } catch (Exception e) {
            log.error("发布消息已推送事件失败,MessageEvent:{}", JSON.toJSONString(message), e);
        }
    }
}
