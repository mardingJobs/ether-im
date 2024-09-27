package cn.ether.im.common.model.message;

import cn.ether.im.common.enums.ImMessageType;
import com.alibaba.fastjson.JSON;
import lombok.Data;

/**
 * * @Author: Martin
 * * @Date    2024/9/14 18:17
 * * @Description
 **/
@Data
public class ImMessageWrapper<T> extends ImMessageTypeWrapper {

    /**
     * 系统消息或者对话消息
     */
    private T message;

    public ImMessageWrapper() {
    }

    public ImMessageWrapper(ImMessageType type, T message) {
        this.setType(type);
        this.message = message;
    }

    public static ImMessageWrapper parseObject(String json) {
        ImMessageTypeWrapper imMessageTypeWrapper = JSON.parseObject(json, ImMessageTypeWrapper.class);
        ImMessageType type = imMessageTypeWrapper.getType();
        switch (type) {
            case CHAT:
                ImChatMessage chatMessage = JSON.parseObject(json, ImChatMessage.class);
                return new ImMessageWrapper<>(type, chatMessage);
            case SYSTEM:
                ImSystemMessage systemMessage = JSON.parseObject(json, ImSystemMessage.class);
                return new ImMessageWrapper<>(type, systemMessage);
            default:
                return null;
        }
    }

}
