package cn.ether.im.push.handler;

import cn.ether.im.common.model.message.ImSystemMessage;
import cn.ether.im.push.processor.MessageProcessor;
import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * * @Author: Martin
 * * @Date    2024/9/3 15:23
 * * @Description
 **/

public class ImChannelHandler extends SimpleChannelInboundHandler<ImSystemMessage> {

    private final Logger logger = LoggerFactory.getLogger(ImChannelHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ImSystemMessage msg) throws Exception {
        logger.info("IMChannelHandler channelRead0: {}", JSON.toJSONString(msg));
        MessageProcessor.processSystemMessage(ctx, msg);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        ChannelId id = ctx.channel().id();
        logger.info("IMChannelHandler.handlerAdded|{}连接", id.asLongText());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        ChannelId id = ctx.channel().id();
        logger.info("IMChannelHandler.handlerRemoved|{}断开连接", id.asLongText());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ChannelId id = ctx.channel().id();
        logger.info("IMChannelHandler.channelActive|{}连接", id.asLongText());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ChannelId id = ctx.channel().id();
        logger.info("IMChannelHandler.channelInactive|{}断开连接", id.asLongText());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("IMChannelHandler.exceptionCaught|异常:{}", cause.getMessage());
    }
}
