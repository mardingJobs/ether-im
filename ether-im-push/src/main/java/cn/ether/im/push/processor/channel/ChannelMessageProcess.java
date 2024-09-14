package cn.ether.im.push.processor.channel;

import cn.ether.im.common.model.message.ImMessage;
import io.netty.channel.ChannelHandlerContext;


/**
 * 处理从管道来的消息
 */
public interface ChannelMessageProcess {


    void process(ChannelHandlerContext ctx, ImMessage message);

}
