package cn.ether.im.client.common.proto;

import cn.ether.im.client.common.enums.ImInfoType;
import cn.ether.im.client.common.exception.ImProtocException;
import cn.ether.im.client.common.model.ImInfo;
import cn.ether.im.common.model.info.ImHeartbeatInfo;
import cn.ether.im.common.model.message.ImChatMessage;
import cn.ether.im.common.model.message.ImGroupMessage;
import cn.ether.im.common.model.message.ImMessageReceived;
import cn.ether.im.common.model.message.ImSingleMessage;
import cn.ether.im.proto.binary.ImBinary;
import cn.ether.im.proto.text.ImTextProto;
import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.protobuf.Any;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.StringValue;
import com.google.protobuf.util.JsonFormat;

import java.util.Objects;

/**
 * * @Author: Martin(微信：martin-jobs)
 * * @Date    2024/10/6 14:39
 * * @Description
 * * @Github <a href="https://github.com/mardingJobs">Github链接</a>
 **/
public class ImProtoDecoder {


    public static ImInfo decodeToImInfo(byte[] bytes) throws InvalidProtocolBufferException {
        ImBinary.ImBinaryProto imBinaryProto = ImBinary.ImBinaryProto.parseFrom(bytes);
        Any body = imBinaryProto.getBody();
        ImInfoType infoType = ImInfoType.valueOf(imBinaryProto.getType());
        ImInfo<Object> imInfo = new ImInfo<>(infoType);
        if (infoType == ImInfoType.HEART_BEAT) {
            return imInfo;
        } else if (infoType == ImInfoType.COMMON) {
            imInfo.setInfo(body.unpack(StringValue.class));
        } else if (infoType == ImInfoType.MESSAGE_RECEIVED) {
            ImBinary.ImMessageReceived messageReceivedBinary = body.unpack(ImBinary.ImMessageReceived.class);
            ImMessageReceived imMessageReceived = new ImMessageReceived(messageReceivedBinary.getMessageId());
            imInfo.setInfo(imMessageReceived);
        } else if (infoType == ImInfoType.SINGLE) {
            ImBinary.ImChatMessage chatMessageBinary = body.unpack(ImBinary.ImChatMessage.class);
            String json = JsonFormat.printer().print(chatMessageBinary);
            ImChatMessage chatMessage = new Gson().fromJson(json, ImSingleMessage.class);
            imInfo.setInfo(chatMessage);
        } else if (infoType == ImInfoType.GROUP) {
            ImBinary.ImChatMessage chatMessageBinary = body.unpack(ImBinary.ImChatMessage.class);
            String json = JsonFormat.printer().print(chatMessageBinary);
            ImChatMessage chatMessage = new Gson().fromJson(json, ImGroupMessage.class);
            imInfo.setInfo(chatMessage);
        } else {
            throw new ImProtocException("不支持的协议类型:" + infoType);
        }

        return imInfo;
    }

    public static ImInfo decodeToImInfo(ImTextProto imTextProto) throws ImProtocException {
        String infoType = imTextProto.getType();
        Object body = imTextProto.getBody();
        String jsonString = JSON.toJSONString(body);
        Object info = null;
        if (Objects.equals(infoType, ImInfoType.HEART_BEAT.name())) {
            info = JSON.parseObject(jsonString, ImHeartbeatInfo.class);
        } else if (Objects.equals(infoType, ImInfoType.COMMON.name())) {
            info = body;
        } else if (Objects.equals(infoType, ImInfoType.MESSAGE_RECEIVED.name())) {
            info = JSON.parseObject(jsonString, ImMessageReceived.class);
        } else if (Objects.equals(infoType, ImInfoType.SINGLE.name())) {
            info = JSON.parseObject(jsonString, ImSingleMessage.class);
        } else if (Objects.equals(infoType, ImInfoType.GROUP.name())) {
            info = JSON.parseObject(jsonString, ImGroupMessage.class);
        } else {
            throw new ImProtocException("不支持的协议类型:" + infoType);
        }
        ImInfo<Object> imInfo = new ImInfo<>();
        imInfo.setType(ImInfoType.valueOf(infoType));
        imInfo.setInfo(info);
        return imInfo;
    }
}
