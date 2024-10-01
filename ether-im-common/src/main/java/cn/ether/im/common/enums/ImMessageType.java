package cn.ether.im.common.enums;

/**
 * 对话消息类型
 */
public enum ImMessageType {

    /**
     * 1对1用户聊天
     */
    PERSONAL("单聊"),
    /**
     * 多对多用户聊天
     */
    GROUP("群聊"),
    /**
     * 系统给用户发送通知，比如在客服系统中，当客服变更时，给用户发送通知消息
     */
    SYS_NOTICE("系统通知");


    private final String desc;

    ImMessageType(String desc) {
        this.desc = desc;
    }

}