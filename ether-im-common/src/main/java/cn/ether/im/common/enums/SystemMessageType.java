package cn.ether.im.common.enums;

import cn.ether.im.common.model.message.*;

/**
 * 系统消息类型
 */
public enum SystemMessageType {

    TOKEN("TOKEN", ImTokenMessage.class),
    HEART_BEAT("心跳", ImHeartbeatMessage.class),
    EVENT("事件", ImMessageEvent.class),
    ACK("ACK", ImSystemAckMessage.class);

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