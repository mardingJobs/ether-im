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


    public static ImMessage parseObject(String json) {
        ImMessageType imMessageType = JSON.parseObject(json, ImMessageType.class);
        ImMessageTypeEnum type = imMessageType.getType();
        ImMessage message = (ImMessage) imMessageType;
        switch (type) {
            case CHAT:
                ImChatMessage chatMessage = JSON.parseObject(json, ImChatMessage.class);
                message.setMessage(chatMessage);
            case SYSTEM:
                ImSystemMessage imSystemMessage = JSON.parseObject(json, ImSystemMessage.class);
                message.setMessage(imSystemMessage);
            default:
        }
        return message;
    }

}
