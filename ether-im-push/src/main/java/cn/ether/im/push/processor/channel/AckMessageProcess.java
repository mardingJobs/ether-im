package cn.ether.im.push.processor.channel;

import cn.ether.im.common.model.message.ImMessage;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 客户端确认收到消息
 * * @Author: Martin
 * * @Date    2024/9/14 23:53
 * * @Description
 **/
@Slf4j
@Component
public class AckMessageProcess implements ChannelMessageProcess {

    @Override
    public void process(ChannelHandlerContext ctx, ImMessage message) {
        String id = message.getId();
        log.info("终端已收到消息:{}", id);
    }
}
