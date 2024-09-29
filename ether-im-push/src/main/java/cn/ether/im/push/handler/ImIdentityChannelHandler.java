package cn.ether.im.push.handler;

import cn.ether.im.common.constants.ImConstants;
import cn.ether.im.common.model.info.ImTokenMessage;
import cn.ether.im.common.util.SpringContextHolder;
import cn.ether.im.push.processor.sys.TokenMessageProcess;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * * @Author: Martin(微信：martin-jobs)
 * * @Date    2024/9/21 21:26
 * * @Description
 * * @Github <a href="https://github.com/mardingJobs">Github链接</a>
 **/
@Slf4j
public class ImIdentityChannelHandler extends SimpleChannelInboundHandler<Object> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 传递给下一个handler
        ctx.fireChannelRead(msg);
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
