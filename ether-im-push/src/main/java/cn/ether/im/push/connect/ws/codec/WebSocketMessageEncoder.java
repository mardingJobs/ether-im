package cn.ether.im.push.connect.ws.codec;

import cn.ether.im.common.model.info.ImInfo;
import cn.ether.im.common.model.protoc.ImProtoc;
import cn.ether.im.common.model.protoc.ImProtocType;
import cn.ether.im.push.util.ChannelHandlerContextUtil;
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
public class WebSocketMessageEncoder extends MessageToMessageEncoder<ImInfo> {

    @Override
    protected void encode(ChannelHandlerContext ctx, ImInfo imInfo, List<Object> out) throws Exception {
        ImProtoc protoc = (ImProtoc) ChannelHandlerContextUtil.getAttr(ctx, "protoc");
        ImProtocType protocType = protoc.getHeader().getType();
        if (protocType == ImProtocType.JSON) {
            TextWebSocketFrame socketFrame = new TextWebSocketFrame(JSON.toJSONString(imInfo));
            out.add(socketFrame);
        }
    }
}
