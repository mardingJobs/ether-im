package cn.ether.im.push.processor.system;

import cn.ether.im.common.event.ImMessageEventType;
import cn.ether.im.common.model.message.ImMessageEvent;
import cn.ether.im.push.mq.MessageEventMQProducer;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 终端确认收到消息
 * * @Author: Martin
 * * @Date    2024/9/14 23:53
 * * @Description
 **/
@Slf4j
@Component
public class MessageEventProcess implements SystemMessageProcess<ImMessageEvent> {

    @Autowired
    private MessageEventMQProducer eventProducer;

    @Override
    public void process(ChannelHandlerContext ctx, ImMessageEvent messageEvent) {
        // 判断能否自己处理
        Long messageId = messageEvent.getMessageId();
        ImMessageEventType eventType = messageEvent.getEventType();
        if (eventType == ImMessageEventType.REACHED || eventType == ImMessageEventType.READ) {

        }


    }
}
