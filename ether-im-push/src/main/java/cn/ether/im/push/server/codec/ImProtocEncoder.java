package cn.ether.im.push.server.codec;

import cn.ether.im.common.model.protoc.ImProtoc;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.List;

/**
 * * @Author: Martin(微信：martin-jobs)
 * * @Date    2024/10/1 02:00
 * * @Description
 * * @Github <a href="https://github.com/mardingJobs">Github链接</a>
 **/
public class ImProtocEncoder extends MessageToMessageEncoder<ImProtoc> {

    @Override
    protected void encode(ChannelHandlerContext ctx, ImProtoc msg, List<Object> out) throws Exception {
        TextWebSocketFrame socketFrame = new TextWebSocketFrame(msg.getType().name());
        out.add(socketFrame);
    }
}
