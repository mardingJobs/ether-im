package cn.ether.im.common.model.message;

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
     * 表示系统消息的唯一标识 (可选)
     */
    private String seq;

    /**
     * 消息类型
     */
    private ImSystemMessageType type;


    public static ImSystemMessage parseObject(String json) {
        ImSystemMessage imSystemMessage = JSON.parseObject(json, ImSystemMessage.class);
        ImSystemMessageType messageType = imSystemMessage.getType();
        Class<? extends ImSystemMessage> messageClass = messageType.getMessageClass();
        return JSON.parseObject(json, messageClass);
    }

    /**
     * 获取唯一标识
     *
     * @return
     */
    @Override
    public String getUniqueId() {
        return seq;
    }
}
