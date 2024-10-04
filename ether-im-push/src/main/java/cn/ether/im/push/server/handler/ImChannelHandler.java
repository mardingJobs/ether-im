package cn.ether.im.push.server.handler;

import cn.ether.im.common.constants.ImConstants;
import cn.ether.im.common.model.info.ImInfo;
import cn.ether.im.common.model.info.sys.ImHeartbeatInfo;
import cn.ether.im.common.model.user.ImUserTerminal;
import cn.ether.im.common.util.SpringContextHolder;
import cn.ether.im.push.cache.UserChannelCache;
import cn.ether.im.push.handler.ImUserContextHandler;
import cn.ether.im.push.processor.ImInfoProcessorContext;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * * @Author: Martin
 * * @Date    2024/9/3 15:23
 * * @Description
 **/

public class ImChannelHandler extends SimpleChannelInboundHandler<ImInfo> {

    private final Logger logger = LoggerFactory.getLogger(ImChannelHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ImInfo msg) throws Exception {
        ImInfoProcessorContext context = SpringContextHolder.getBean(ImInfoProcessorContext.class);
        context.process(ctx, msg);
    }


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state().equals(IdleState.READER_IDLE)) {
                Attribute<Integer> heartBeatTimesKey = ctx.channel().attr(AttributeKey.valueOf(ImConstants.HEARTBEAT_TIMES));
                Integer failedTimes = heartBeatTimesKey.get();
                if (failedTimes == null || failedTimes < ImConstants.HEARTBEAT_MAX_TIMES) {
                    ImHeartbeatInfo heartbeatMessage = new ImHeartbeatInfo();
                    ctx.writeAndFlush(heartbeatMessage);
                    heartBeatTimesKey.set(failedTimes == null ? 1 : failedTimes + 1);
                } else {
                    logger.info("网络连接已失效,断开连接。RemoteAddress:{}", ctx.channel().remoteAddress());
                    ctx.close();
                }
            }
        }
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("channelActive|{}连接", ctx.channel().remoteAddress());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ImUserTerminal userTerminal = UserChannelCache.getUserTerminal(ctx);
        if (userTerminal == null) {
            ctx.close();
            return;
        }
        logger.info("channelInactive|{}断开连接", ctx.channel().remoteAddress());
        ImUserContextHandler userContextHandler = SpringContextHolder.getBean(ImUserContextHandler.class);
        userContextHandler.clearUserContext(userTerminal, ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.error("exceptionCaught|异常", cause);
        } else {
            logger.error("exceptionCaught|异常:{}", cause.getMessage());
        }
    }
}
