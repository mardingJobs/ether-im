package cn.ether.im.client.common.proto;

import cn.ether.im.client.common.enums.ImInfoType;
import cn.ether.im.client.common.exception.ImProtocException;
import cn.ether.im.client.common.model.ImInfo;
import cn.ether.im.common.enums.ImMessageContentType;
import cn.ether.im.common.enums.ImMessageType;
import cn.ether.im.common.enums.ImTerminalType;
import cn.ether.im.common.model.message.ImMessage;
import cn.ether.im.common.model.message.ImMessageReceived;
import cn.ether.im.common.model.user.ImUserTerminal;
import cn.ether.im.proto.binary.ImBinary;
import cn.ether.im.proto.text.ImMessageTextProto;
import cn.ether.im.proto.text.ImTextProto;
import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import lombok.extern.slf4j.Slf4j;


/**
 * * @Author: Martin(微信：martin-jobs)
 * * @Date    2024/10/2 15:33
 * * @Description
 * * @Github <a href="https://github.com/mardingJobs">Github链接</a>
 **/
@Slf4j
public class ImProtoConverter {


    public static ImTextProto parseBytes(byte[] bytes) throws InvalidProtocolBufferException {
        ImBinary.ImBinaryProto imBinaryProto = ImBinary.ImBinaryProto.parseFrom(bytes);
        ImTextProto imTextProto = ImProtoConverter.binaryToTextProto(imBinaryProto);
        return imTextProto;
    }

    public static ImBinary.ImMessageBinaryProto textToBinaryProto(ImMessageTextProto messageTextProto) {
        String jsonString = JSON.toJSONString(messageTextProto);
        ImBinary.ImMessageBinaryProto.Builder builder = JSON.parseObject(jsonString, ImBinary.ImMessageBinaryProto.newBuilder().getClass());
        return builder.build();
    }

    public static ImBinary.ImBinaryProto textToBinaryProto(ImTextProto textProto) {
        String jsonString = JSON.toJSONString(textProto);
        ImBinary.ImBinaryProto.Builder builder = JSON.parseObject(jsonString, ImBinary.ImBinaryProto.newBuilder().getClass());
        return builder.build();
    }


    public static ImTextProto binaryToTextProto(ImBinary.ImBinaryProto binaryProto) throws InvalidProtocolBufferException {
        String json = JsonFormat.printer().print(binaryProto);
        return new Gson().fromJson(json, ImTextProto.class);
    }


    public static ImTextProto encodeToTextProto(ImInfo imInfo) throws ImProtocException {
        ImTextProto imTextProto = new ImTextProto();
        try {
            imTextProto.setType(imInfo.getType().getCode());
            ImMessageTextProto imMessageTextProto = new ImMessageTextProto();
            imTextProto.setBody(imMessageTextProto);
            if (imInfo.getType() == ImInfoType.COMMON) {

            } else if (imInfo.getType() == ImInfoType.MESSAGE) {
                ImMessage message = (ImMessage) imInfo.getInfo();
                imMessageTextProto.setMessageId(message.getId());
                imMessageTextProto.setMessageType(message.getMessageType().getCode());
                imMessageTextProto.setContentType(message.getContentType().getCode());
                imMessageTextProto.setSenderId(message.getSender().getUserId());
                imMessageTextProto.setSenderTerminal(message.getSender().getTerminalType().getCode());
                imMessageTextProto.setContent(message.getContent());
                imMessageTextProto.setSendTime(message.getSendTime());
            } else if (imInfo.getType() == ImInfoType.MESSAGE_WITH_DRAWN_NOTICE
                    || imInfo.getType() == ImInfoType.MESSAGE_READ_NOTICE) {

            }
        } catch (Exception e) {
            log.info("转换为文本协议失败", e);
            throw new ImProtocException("转换为文本协议失败");
        }
        if (log.isDebugEnabled()) {
            log.debug("encodeToTextProto|{}", imTextProto);
        }
        return imTextProto;
    }


    public static ImInfo decodeToImInfo(ImTextProto imTextProto) throws ImProtocException {
        ImInfo<Object> imInfo = new ImInfo<>();
        int type = imTextProto.getType();
        ImMessageTextProto body = imTextProto.getBody();
        ImInfoType infoType = ImInfoType.getByCode(type);
        imInfo.setType(infoType);
        if (infoType == null) {
            throw new ImProtocException("不支持的协议");
        } else if (infoType == ImInfoType.COMMON) {
            imInfo.setInfo(body.getContent());
            return imInfo;
        } else if (infoType == ImInfoType.MESSAGE) {
            ImMessage imMessage = new ImMessage();
            imMessage.setId(body.getMessageId());
            imMessage.setSender(new ImUserTerminal(body.getSenderId(), ImTerminalType.fromCode(body.getSenderTerminal())));
            imMessage.setContent(body.getContent());
            ImMessageContentType contentType = ImMessageContentType.getByCode(body.getContentType());
            imMessage.setContentType(contentType);
            imMessage.setSendTime(body.getSendTime());
            Integer typeCode = body.getMessageType();
            ImMessageType messageType = ImMessageType.getByCode(typeCode);
            if (messageType == null) {
                throw new ImProtocException("不支持的消息类型:" + typeCode);
            }
            imMessage.setMessageType(messageType);
            imInfo.setInfo(imMessage);
        } else if (infoType == ImInfoType.MESSAGE_RECEIVED) {
            ImMessageReceived imMessageReceived = new ImMessageReceived();
            imMessageReceived.setMessageId(body.getMessageId());
        } else {
            throw new ImProtocException("不支持的消息类型");
        }
        return imInfo;
    }
}
