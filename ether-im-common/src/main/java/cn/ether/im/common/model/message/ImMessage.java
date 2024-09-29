package cn.ether.im.common.model.message;

import cn.ether.im.common.enums.ImMessageType;
import com.alibaba.fastjson.JSON;
import lombok.Data;

/**
 * * @Author: Martin(微信：martin-jobs)
 * * @Date    2024/9/18 16:23
 * * @Description
 * * @Github https://github.com/mardingJobs
 **/
@Data
public class ImMessage implements IdentifiableMessage, ImChannelMessage {

    /**
     * 表示消息的唯一标识 (可选)
     * 对于对话消息，是消息ID；对于系统消息，是可以不是消息ID
     */
    private String uid;

    private ImMessageType messageType;

    public static ImMessage parseObject(String json) {
        ImMessage message = JSON.parseObject(json, ImMessage.class);
        ImMessageType messageType = message.getMessageType();
        if (messageType == ImMessageType.CHAT) {
            return JSON.parseObject(json, ImChatMessage.class);
        } else if (messageType == ImMessageType.SYSTEM) {
            return ImSystemMessage.parseObject(json);
        }
        throw new IllegalArgumentException("不支持的消息类型:" + messageType);
    }

    public String uid() {
        return uid;
    }

}
