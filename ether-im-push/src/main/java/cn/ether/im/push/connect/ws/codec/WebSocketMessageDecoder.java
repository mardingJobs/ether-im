package cn.ether.im.push.connect.ws.codec;

import cn.ether.im.common.model.message.ImMessage;
import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * * @Author: Martin
 * * @Date    2024/9/3 14:59
 * * @Description
 **/
@Slf4j
public class WebSocketMessageDecoder extends MessageToMessageDecoder<TextWebSocketFrame> {


    @Override
    protected void decode(ChannelHandlerContext ctx, TextWebSocketFrame msg, List<Object> out) throws Exception {
        String text = msg.text();
        log.info("WebSocketMessageDecoder decode|{}",text);
        ImMessage message = JSON.parseObject(text, ImMessage.class);
        out.add(message);
    }
}