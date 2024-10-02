package cn.ether.im.common.enums;

import lombok.Getter;

/**
 * 对话消息类型
 */
public enum ImMessageType {

    /**
     * 1对1用户聊天
     */
    PERSONAL(0, "单聊"),
    /**
     * 多对多用户聊天
     */
    GROUP(1, "群聊"),
    /**
     * 系统给用户发送通知，比如在客服系统中，当客服变更时，给用户发送通知消息
     */
    SYS_NOTICE(3, "系统通知");

    @Getter
    private final int code;
    private final String desc;

    ImMessageType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static ImMessageType getByCode(int code) {
        for (ImMessageType type : values()) {
            if (type.code == code) {
                return type;
            }
        }
        return null;
    }

}