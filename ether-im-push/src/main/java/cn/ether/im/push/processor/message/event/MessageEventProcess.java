package cn.ether.im.push.processor.message.event;

import cn.ether.im.common.model.info.message.event.ImMessageEvent;
import cn.ether.im.common.model.info.message.event.ImMessageEventType;
import cn.ether.im.push.mq.ImMessageEventMQProducer;
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
public class MessageEventProcess {

    @Autowired
    private ImMessageEventMQProducer eventProducer;

    public void process(ChannelHandlerContext ctx, ImMessageEvent messageEvent) {
        // 判断能否自己处理
        Long messageId = messageEvent.getMessageId();
        ImMessageEventType eventType = messageEvent.getEventType();
        if (eventType == ImMessageEventType.REACHED || eventType == ImMessageEventType.READ) {

        }


    }
}
