package cn.ether.im.push.processor;

import cn.ether.im.common.model.ImUser;
import cn.ether.im.common.model.message.ImMessage;
import cn.ether.im.push.cache.UserChannelCache;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * * @Author: Martin
 * * @Date    2024/9/14 23:22
 * * @Description
 **/
@Slf4j
@Component
public class PersonalMessageProcessor implements MessageProcessor {

    /**
     * 处理非长连接收到的消息数据
     *
     * @param message
     */
    @Override
    public void process(ImMessage message) {
        ImUser receiver = message.getReceivers().get(0);
        ChannelHandlerContext channelHandlerContext = UserChannelCache.getChannelCtx(receiver.getUserId(), receiver.getTerminalType().toString());
        if (channelHandlerContext != null) {
            channelHandlerContext.writeAndFlush(message);
        } else {
            log.error("无接收者管道，消息接收者：{},消息ID：{}", receiver.getUserId(), message.getId());
        }
    }
}
