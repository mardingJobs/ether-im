package cn.ether.im.common.model.message;

import cn.ether.im.common.enums.SystemMessageType;
import com.alibaba.fastjson.JSON;
import lombok.Data;

/**
 * * @Author: Martin
 * * @Date    2024/9/16 01:06
 * * @Description
 **/
@Data
public class ImSystemMessage implements ImChannelMessage {

    /**
     * 消息类型
     */
    private SystemMessageType type;


    public static ImSystemMessage parseObject(String json) {
        ImSystemMessage imSystemMessage = JSON.parseObject(json, ImSystemMessage.class);
        SystemMessageType messageType = imSystemMessage.getType();
        Class<? extends ImSystemMessage> messageClass = messageType.getMessageClass();
        return JSON.parseObject(json, messageClass);
    }
}
