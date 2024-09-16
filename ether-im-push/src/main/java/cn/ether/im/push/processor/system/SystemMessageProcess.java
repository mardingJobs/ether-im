package cn.ether.im.push.processor.system;

import cn.ether.im.common.model.message.ImSystemMessage;
import io.netty.channel.ChannelHandlerContext;


/**
 *
 */
public interface SystemMessageProcess<T extends ImSystemMessage> {


    void process(ChannelHandlerContext ctx, T message);


}