package cn.ether.im.push.connect.ws.codec;

import cn.ether.im.common.model.info.ImInfo;
import cn.ether.im.push.connect.ImProtocolConverter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
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
            ImInfo imInfo = ImInfo.parseObject(text);
            out.add(imInfo);
        } else if (socketFrame instanceof BinaryWebSocketFrame) {
            byte[] bytes = new byte[socketFrame.content().readableBytes()];
            socketFrame.content().readBytes(bytes);
            ImInfo imInfo = ImProtocolConverter.decodeToImInfo(bytes);
            out.add(imInfo);
        }
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
