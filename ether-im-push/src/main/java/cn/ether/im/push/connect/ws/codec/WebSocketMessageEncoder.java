package cn.ether.im.push.connect.ws.codec;

import cn.ether.im.common.model.info.ImInfo;
import cn.ether.im.common.model.protoc.ImProtoc;
import cn.ether.im.common.model.protoc.ImProtocType;
import cn.ether.im.push.connect.ImProtocParser;
import cn.ether.im.push.util.ChannelHandlerContextUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * * @Author: Martin
 * * @Date    2024/9/3 14:59
 * * @Description
 **/
@Slf4j
public class WebSocketMessageEncoder extends MessageToMessageEncoder<ImInfo> {

    @Override
    protected void encode(ChannelHandlerContext ctx, ImInfo imInfo, List<Object> out) throws Exception {
        ImProtoc protoc = (ImProtoc) ChannelHandlerContextUtil.getAttr(ctx, "protoc");
        ImProtocType protocType = protoc.getType();
        if (ImProtocType.JSON.equals(protocType)) {
            protoc.setBody(imInfo);
            String text = ImProtocParser.toText(protoc);
            TextWebSocketFrame socketFrame = new TextWebSocketFrame(text);
            out.add(socketFrame);
        }
    }
}
