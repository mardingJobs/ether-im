package cn.ether.im.push.connect.ws.codec;

import cn.ether.im.common.model.info.ImInfo;
import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * * @Author: Martin
 * * @Date    2024/9/3 14:59
 * * @Description
 **/
@Slf4j
public class WebSocketMessageDecoder extends MessageToMessageDecoder<TextWebSocketFrame> {


    @Override
    protected void decode(ChannelHandlerContext ctx, TextWebSocketFrame socketFrame, List<Object> out) throws Exception {
        String text = socketFrame.text();
        if (StringUtils.isEmpty(text)) {
            return;
        }
        ImInfo imInfo = JSON.parseObject(text, ImInfo.class);
        out.add(imInfo);
    }
}
