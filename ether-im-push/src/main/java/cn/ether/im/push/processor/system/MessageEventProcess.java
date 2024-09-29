package cn.ether.im.push.processor.system;

import cn.ether.im.common.event.MessageEventBroadcast;
import cn.ether.im.common.model.message.ImMessageEvent;
import cn.ether.im.push.mq.ImMessageEventDefaultMqListener;
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
    private MessageEventBroadcast messageBroadcast;

    @Autowired
    private ImMessageEventDefaultMqListener eventProducer;

    @Override
    public void process(ChannelHandlerContext ctx, ImMessageEvent messageEvent) {
        messageBroadcast.broadcast(messageEvent);
    }
}
