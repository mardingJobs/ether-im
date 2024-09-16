package cn.ether.im.common.model.message;

import cn.ether.im.common.enums.ImMessageTypeEnum;
import com.alibaba.fastjson.JSON;
import lombok.Data;

/**
 * * @Author: Martin
 * * @Date    2024/9/14 18:17
 * * @Description
 **/
@Data
public class ImMessage<T> extends ImMessageType {

    /**
     * 系统消息或者对话消息
     */
    private T message;

    public ImMessage() {
    }

    public ImMessage(ImMessageTypeEnum type, T message) {
        this.setType(type);
        this.message = message;
    }

    public static ImMessage parseObject(String json) {
        ImMessageType imMessageType = JSON.parseObject(json, ImMessageType.class);
        ImMessageTypeEnum type = imMessageType.getType();
        switch (type) {
            case CHAT:
                ImChatMessage chatMessage = JSON.parseObject(json, ImChatMessage.class);
                return new ImMessage<>(type, chatMessage);
            case SYSTEM:
                ImSystemMessage systemMessage = JSON.parseObject(json, ImSystemMessage.class);
                return new ImMessage<>(type, systemMessage);
            default:
                return null;
        }
    }

}
