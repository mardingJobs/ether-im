package cn.ether.im.push.server.codec;

import cn.ether.im.client.common.model.ImInfo;
import cn.ether.im.client.common.proto.ImProtoDecoder;
import cn.ether.im.proto.text.ImTextProto;
import com.alibaba.fastjson.JSON;
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
        ImInfo imInfo = null;
        if (socketFrame instanceof TextWebSocketFrame) {
            String text = ((TextWebSocketFrame) socketFrame).text();
            if (StringUtils.isEmpty(text)) {
                return;
            }
            ImTextProto imTextProto = JSON.parseObject(text, ImTextProto.class);
            imInfo = ImProtoDecoder.decodeToImInfo(imTextProto);
        } else if (socketFrame instanceof BinaryWebSocketFrame) {
            byte[] bytes = new byte[socketFrame.content().readableBytes()];
            socketFrame.content().readBytes(bytes);
            imInfo = ImProtoDecoder.decodeToImInfo(bytes);
        }
        if (imInfo != null) {
            if (log.isDebugEnabled()) {
                log.debug("decode|imInfo:{}", JSON.toJSONString(imInfo));
            }
            out.add(imInfo);
        }
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
