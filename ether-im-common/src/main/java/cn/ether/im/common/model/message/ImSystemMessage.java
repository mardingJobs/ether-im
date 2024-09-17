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

    private static final long serialVersionUID = 1L;

    /**
     * 表示系统消息的唯一标识 (可选)
     */
    private String seq;

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
