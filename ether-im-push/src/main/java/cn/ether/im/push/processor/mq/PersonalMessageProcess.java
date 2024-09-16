package cn.ether.im.push.processor.mq;

import cn.ether.im.common.model.message.ImChatMessage;
import cn.ether.im.common.model.user.ImUserTerminal;
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
public class PersonalMessageProcess implements ChatMessageProcess {


    @Override
    public void process(ImChatMessage message) {
        ImUserTerminal receiver = (ImUserTerminal) message.getReceivers().get(0);
        ChannelHandlerContext channelHandlerContext = UserChannelCache.getChannelCtx(receiver.getUserId(),
                receiver.getTerminalType().toString());
        if (channelHandlerContext != null) {
            channelHandlerContext.writeAndFlush(message);
        } else {
            log.error("无接收者管道，消息接收者：{},消息ID：{}", receiver.getUserId(), message.getId());
        }
    }
}
