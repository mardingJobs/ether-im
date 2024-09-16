package cn.ether.im.common.enums;

import cn.ether.im.common.model.message.ImChatMessageAck;
import cn.ether.im.common.model.message.ImHeartbeatMessage;
import cn.ether.im.common.model.message.ImSystemMessage;
import cn.ether.im.common.model.message.ImTokenMessage;

/**
 * 系统消息类型
 */
public enum SystemMessageType {

    TOKEN("TOKEN", ImTokenMessage.class),
    HEART_BEAT("心跳", ImHeartbeatMessage.class),
    ACK_RECEIVE("消息确认", ImChatMessageAck.class);

    private final String desc;
    private final Class<? extends ImSystemMessage> messageClass;

    private SystemMessageType(String desc, Class messageClass) {
        this.desc = desc;
        this.messageClass = messageClass;
    }

    public String getDesc() {
        return desc;
    }

    public Class<? extends ImSystemMessage> getMessageClass() {
        return messageClass;
    }
}