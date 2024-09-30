package cn.ether.im.push.connect.ws.codec;

import cn.ether.im.common.model.protoc.ImProtoc;
import cn.ether.im.common.model.protoc.ImProtocHeader;
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
        ImProtocHeader header = msg.getHeader();
        TextWebSocketFrame socketFrame = new TextWebSocketFrame(header.getType().name());
        out.add(socketFrame);
    }
}
