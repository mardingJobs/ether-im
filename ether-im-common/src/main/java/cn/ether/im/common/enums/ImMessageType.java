package cn.ether.im.common.enums;

import cn.ether.im.common.model.message.ImChatMessage;
import cn.ether.im.common.model.message.ImMessage;
import cn.ether.im.common.model.message.ImSystemMessage;

/**
 * 消息类型
 */
public enum ImMessageType {

    CHAT("对话", ImChatMessage.class),
    SYSTEM("系统", ImSystemMessage.class);

    private final String desc;
    private final Class<? extends ImMessage> messageClass;

    private ImMessageType(String desc, Class<? extends ImMessage> messageClass) {
        this.desc = desc;
        this.messageClass = messageClass;
    }

    public Class<? extends ImMessage> getMessageClass() {
        return messageClass;
    }
}