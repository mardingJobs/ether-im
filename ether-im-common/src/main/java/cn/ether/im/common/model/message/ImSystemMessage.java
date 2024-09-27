package cn.ether.im.common.model.message;

import cn.ether.im.common.enums.ImMessageType;
import cn.ether.im.common.enums.ImSystemMessageType;
import com.alibaba.fastjson.JSON;
import lombok.Data;

/**
 * * @Author: Martin
 * * @Date    2024/9/16 01:06
 * * @Description
 **/
@Data
public class ImSystemMessage extends ImMessage {

    private static final long serialVersionUID = 1L;

    /**
     * 消息类型
     */
    private ImSystemMessageType systemMessageType;

    public ImSystemMessage() {
        this.setMessageType(ImMessageType.SYSTEM);
    }

    public ImSystemMessage(ImSystemMessageType systemMessageType) {
        this();
        this.systemMessageType = systemMessageType;
    }

    public static ImSystemMessage parseObject(String json) {
        ImSystemMessage imSystemMessage = JSON.parseObject(json, ImSystemMessage.class);
        ImSystemMessageType messageType = imSystemMessage.getSystemMessageType();
        Class<? extends ImSystemMessage> messageClass = messageType.getMessageClass();
        return JSON.parseObject(json, messageClass);
    }
}
