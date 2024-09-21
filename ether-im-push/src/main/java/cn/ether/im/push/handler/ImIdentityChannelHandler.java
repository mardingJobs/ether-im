package cn.ether.im.push.handler;

import cn.ether.im.common.constants.ImConstants;
import cn.ether.im.common.model.message.ImTokenMessage;
import cn.ether.im.common.util.SpringContextHolder;
import cn.ether.im.push.processor.system.TokenMessageProcess;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import org.apache.commons.lang3.StringUtils;

/**
 * * @Author: Martin(微信：martin-jobs)
 * * @Date    2024/9/21 21:26
 * * @Description
 * * @Github <a href="https://github.com/mardingJobs">Github链接</a>
 **/
public class ImIdentityChannelHandler extends SimpleChannelInboundHandler<Object> {

    /**
     * <strong>Please keep in mind that this method will be renamed to
     * {@code messageReceived(ChannelHandlerContext, I)} in 5.0.</strong>
     * <p>
     * Is called for each message of type {@link I}.
     *
     * @param ctx the {@link ChannelHandlerContext} which this {@link SimpleChannelInboundHandler}
     *            belongs to
     * @param msg the message to handle
     * @throws Exception is thrown if an error occurred
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {

    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            WebSocketServerProtocolHandler.HandshakeComplete handshakeComplete = (WebSocketServerProtocolHandler.HandshakeComplete) evt;
            HttpHeaders headers = handshakeComplete.requestHeaders();
            String token = headers.get(ImConstants.IM_TOKEN);
            if (StringUtils.isEmpty(token)) {
                ctx.channel().close();
            }

            TokenMessageProcess bean = SpringContextHolder.getBean(TokenMessageProcess.class);
            bean.process(ctx, new ImTokenMessage(token));
        }
        super.userEventTriggered(ctx, evt);
    }
}
