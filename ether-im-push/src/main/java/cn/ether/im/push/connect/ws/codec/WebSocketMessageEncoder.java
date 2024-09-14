package cn.ether.im.push.connect.ws.codec;

import cn.ether.im.common.model.message.ImMessage;
import com.alibaba.fastjson.JSON;
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
public class WebSocketMessageEncoder extends MessageToMessageEncoder<ImMessage> {

    @Override
    protected void encode(ChannelHandlerContext ctx, ImMessage msg, List<Object> out) throws Exception {
        String jsonString = JSON.toJSONString(msg);
        log.info("WebSocketMessageEncoder.encode|msg:{}", jsonString);
        TextWebSocketFrame socketFrame = new TextWebSocketFrame(jsonString);
        out.add(socketFrame);
    }
}
