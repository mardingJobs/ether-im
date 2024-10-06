package cn.ether.im.client.common.proto;

import cn.ether.im.client.common.enums.ImInfoType;
import cn.ether.im.client.common.exception.ImProtocException;
import cn.ether.im.client.common.model.ImInfo;
import cn.ether.im.common.model.message.ImMessageReceived;
import cn.ether.im.proto.binary.ImBinary;
import cn.ether.im.proto.text.ImTextProto;
import com.alibaba.fastjson.JSON;
import com.google.protobuf.Any;
import com.google.protobuf.StringValue;

/**
 * * @Author: Martin(微信：martin-jobs)
 * * @Date    2024/10/6 14:38
 * * @Description
 * * @Github <a href="https://github.com/mardingJobs">Github链接</a>
 **/
public class ImProtoEncoder {


    public static ImTextProto encodeToText(ImInfo imInfo) throws ImProtocException {
        ImTextProto imTextProto = new ImTextProto();
        imTextProto.setType(imInfo.getType().name());
        imTextProto.setBody(imInfo.getInfo());
        return imTextProto;
    }

    public static ImBinary.ImBinaryProto encodeToBinary(ImInfo imInfo) {
        Object info = imInfo.getInfo();
        ImInfoType type = imInfo.getType();
        ImBinary.ImBinaryProto.Builder builder = ImBinary.ImBinaryProto.newBuilder().setType(type.name());
        if (type == ImInfoType.HEART_BEAT) {
            Any defaultInstance = Any.getDefaultInstance();
            builder.setBody(defaultInstance);
        } else if (type == ImInfoType.COMMON) {
            StringValue stringValue = StringValue.newBuilder().setValue(info.toString()).build();
            builder.setBody(Any.pack(stringValue));
        } else if (type == ImInfoType.SINGLE || type == ImInfoType.GROUP) {
            String jsonString = JSON.toJSONString(info);
            ImBinary.ImChatMessage.Builder chatMessageBuilder = JSON.parseObject(jsonString, ImBinary.ImChatMessage.newBuilder().getClass());
            builder.setBody(Any.pack(chatMessageBuilder.build()));
        } else if (type == ImInfoType.MESSAGE_RECEIVED) {
            ImMessageReceived messageReceived = (ImMessageReceived) info;
            ImBinary.ImMessageReceived received = ImBinary.ImMessageReceived.newBuilder().setMessageId(messageReceived.getMessageId()).build();
            builder.setBody(Any.pack(received));
        } else {
            throw new ImProtocException("不支持的编码类型:" + type);
        }
        return builder.build();
    }
}
