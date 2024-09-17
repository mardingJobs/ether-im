package cn.ether.im.push.processor.system;

import cn.ether.im.common.exception.ImException;
import cn.ether.im.common.model.message.ImMessageEvent;
import cn.ether.im.push.mq.ImMessageEventProducer;
import com.alibaba.fastjson.JSON;
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
    private ImMessageEventProducer eventProducer;

    @Override
    public void process(ChannelHandlerContext ctx, ImMessageEvent message) {
        try {
            eventProducer.publish(message);
        } catch (Exception e) {
            log.error("发布消息事件失败,MessageEvent:{}", JSON.toJSONString(message));
            throw new ImException("发布消息事件失败", e);
        }
    }
}