package cn.ether.im.push.processor.sys;

import cn.ether.im.common.model.info.sys.ImSysMessage;
import io.netty.channel.ChannelHandlerContext;


/**
 *
 */
public interface SystemMessageProcess<T extends ImSysMessage> {


    void process(ChannelHandlerContext ctx, T message);


}
