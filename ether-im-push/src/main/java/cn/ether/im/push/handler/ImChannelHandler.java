package cn.ether.im.push.handler;

import cn.ether.im.common.helper.ImUserContextHelper;
import cn.ether.im.common.model.message.ImSystemMessage;
import cn.ether.im.common.model.user.ImUserTerminal;
import cn.ether.im.common.util.SpringContextHolder;
import cn.ether.im.push.cache.UserChannelCache;
import cn.ether.im.push.processor.MessageProcessor;
import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
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
        logger.info("channelRead0: {}", JSON.toJSONString(msg));
        MessageProcessor.processSystemMessage(ctx, msg);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        ImUserTerminal userTerminal = UserChannelCache.getUserTerminal(ctx);
        if (userTerminal != null) {
            logger.info("chandlerRemoved|{}断开连接", JSON.toJSONString(userTerminal));
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ImUserTerminal userTerminal = UserChannelCache.getUserTerminal(ctx);
        if (userTerminal != null) {
            logger.info("channelActive|{}连接", JSON.toJSONString(userTerminal));
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ImUserTerminal userTerminal = UserChannelCache.getUserTerminal(ctx);
        if (userTerminal == null) {
            return;
        }
        ImUserContextHelper userContextHelper = SpringContextHolder.getBean(ImUserContextHelper.class);
        userContextHelper.removeServerCache(userTerminal);
        UserChannelCache.removeChannelCtx(userTerminal.getUserId(), userTerminal.getTerminalType().name());
        logger.info("channelInactive|{}断开连接", JSON.toJSONString(userTerminal));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("exceptionCaught|异常:{}", cause.getMessage());
    }
}
