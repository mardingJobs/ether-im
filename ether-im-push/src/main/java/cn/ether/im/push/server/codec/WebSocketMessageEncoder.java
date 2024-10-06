package cn.ether.im.push.server.codec;

import cn.ether.im.client.common.model.ImInfo;
import cn.ether.im.client.common.proto.ImProtoEncoder;
import cn.ether.im.common.model.protoc.ImProtoType;
import cn.ether.im.common.model.protoc.ImProtoc;
import cn.ether.im.proto.binary.ImBinary;
import cn.ether.im.proto.text.ImTextProto;
import cn.ether.im.push.util.ChannelHandlerContextUtil;
import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
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
        ImProtoType protocType = protoc.getType();
        if (protocType == ImProtoType.JSON) {
            ImTextProto imTextProto = ImProtoEncoder.encodeToText(imInfo);
            TextWebSocketFrame socketFrame = new TextWebSocketFrame(JSON.toJSONString(imTextProto));
            out.add(socketFrame);
        } else if (protocType == ImProtoType.PROTOC_BUFFER) {
            ImBinary.ImBinaryProto imBinaryProto = ImProtoEncoder.encodeToBinary(imInfo);
            ByteBuf byteBuf = Unpooled.wrappedBuffer(imBinaryProto.toByteArray());
            BinaryWebSocketFrame binaryWebSocketFrame = new BinaryWebSocketFrame(byteBuf);
            out.add(binaryWebSocketFrame);
        }
    }

}
