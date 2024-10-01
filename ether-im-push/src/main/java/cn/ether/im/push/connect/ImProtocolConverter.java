package cn.ether.im.push.connect;

import cn.ether.im.common.enums.ImInfoType;
import cn.ether.im.common.model.info.ImInfo;
import cn.ether.im.common.model.info.MessageReceivedNotice;
import cn.ether.im.common.model.info.message.ImMessage;
import cn.ether.im.common.model.user.ImUserTerminal;
import cn.ether.im.push.exception.ImProtocException;
import cn.ether.im.push.protoc.ImInfoProtocBuffer;
import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.InvalidProtocolBufferException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 协议格式
 * 头：
 * 协议类型(两个字节) 版本号(两个字节) 预留4个字节。
 * 体：
 * 消息体(json)/二进制
 * * @Author: Martin(微信：martin-jobs)
 * * @Date    2024/9/30 13:13
 * * @Description
 * * @Github <a href="https://github.com/mardingJobs">Github链接</a>
 **/
@Slf4j
@Data
public class ImProtocolConverter {


    public static ImInfoProtocBuffer.ImUserTerminal toProtocBuffer(ImUserTerminal userTerminal) {
        return ImInfoProtocBuffer.ImUserTerminal.newBuilder()
                .setTerminalType(userTerminal.getTerminalType().getCode())
                .setUserId(userTerminal.getUserId())
                .build();
    }


    public static GeneratedMessage toProtocBufferEncode(ImInfo imInfo) {
        ImInfoType type = imInfo.getType();
        if (type == ImInfoType.MESSAGE || type == ImInfoType.MESSAGE_READ_NOTICE
                || type == ImInfoType.MESSAGE_WITH_DRAWN_NOTICE_NOTICE) {
            ImMessage message = (ImMessage) imInfo;
            return ImInfoProtocBuffer.ImMessage.newBuilder()
                    .setId(message.getId())
                    .setSender(toProtocBuffer(((ImMessage) imInfo).getSender()))
                    .setType(imInfo.getType().getCode())
                    .setContentType(message.getContentType().getCode())
                    .setSendTime(message.getSendTime())
                    .setContent(message.getContent())
                    .build();
        } else if (type == ImInfoType.INFO || type == ImInfoType.HEART_BEAT || type == ImInfoType.PROTOCOL_SELECT_RESULT) {
            return ImInfoProtocBuffer.ImInfo.newBuilder()
                    .setInfo(imInfo.getInfo())
                    .setType(type.getCode())
                    .build();
        } else if (type == ImInfoType.MESSAGE_RECEIVED) {
            MessageReceivedNotice messageReceivedNotice = (MessageReceivedNotice) imInfo;
            String userId = messageReceivedNotice.getUserId();
            return ImInfoProtocBuffer.MessageReceivedNotice.newBuilder()
                    .setMessageId(messageReceivedNotice.getMessageId())
                    .setUserId(userId)
                    .build();
        } else {
            throw new RuntimeException("不支持的协议类型");
        }
    }


    public static ImInfo decodeToImInfo(byte[] bytes) throws ImProtocException, InvalidProtocolBufferException {
        ImInfoProtocBuffer.ImInfo imInfo = ImInfoProtocBuffer.ImInfo.parseFrom(bytes);
        int t = imInfo.getType();
        if (ImInfoType.INFO.getCode() == t) {
            int type = imInfo.getType();
            String info = imInfo.getInfo();
            ImInfoType infoType = ImInfoType.getByCode(type);
            if (infoType == null) {
                throw new ImProtocException("不支持的消息类型");
            }
            return new ImInfo(infoType, info);
        } else if (ImInfoType.MESSAGE_RECEIVED.getCode() == t) {
            ImInfoProtocBuffer.MessageReceivedNotice receivedNotice = ImInfoProtocBuffer.MessageReceivedNotice.parseFrom(bytes);
            MessageReceivedNotice messageReceivedNotice = new MessageReceivedNotice();
            messageReceivedNotice.setMessageId(receivedNotice.getMessageId());
            messageReceivedNotice.setUserId(receivedNotice.getUserId());
        } else {
            throw new ImProtocException("不支持的消息类型");
        }
        throw new ImProtocException("不支持的消息类型");
    }

}
