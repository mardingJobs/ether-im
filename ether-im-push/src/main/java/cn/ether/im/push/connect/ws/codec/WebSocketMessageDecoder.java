package cn.ether.im.push.connect.ws.codec;

import cn.ether.im.common.model.info.ImInfo;
import cn.ether.im.common.model.protoc.ImProtoc;
import cn.ether.im.push.connect.ImProtocParser;
import cn.ether.im.push.exception.ImProtocException;
import cn.ether.im.push.util.ChannelHandlerContextUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * * @Author: Martin
 * * @Date    2024/9/3 14:59
 * * @Description
 **/
@Slf4j
public class WebSocketMessageDecoder extends MessageToMessageDecoder<WebSocketFrame> {


    @Override
    protected void decode(ChannelHandlerContext ctx, WebSocketFrame socketFrame, List<Object> out) throws Exception {
        if (socketFrame instanceof TextWebSocketFrame) {
            String text = ((TextWebSocketFrame) socketFrame).text();
            if (StringUtils.isEmpty(text)) {
                return;
            }
            ImProtoc imProtoc = null;
            try {
                imProtoc = ImProtocParser.parseText(text);
            } catch (ImProtocException e) {
                ctx.writeAndFlush(new ImInfo());
            }
            ChannelHandlerContextUtil.setAttr(ctx, "protoc", imProtoc);
            out.add(imProtoc.getBody());
        }
    }
}
