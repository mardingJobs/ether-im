package cn.ether.im.common.enums;

/**
 * 系统消息类型
 */
public enum SystemMessageType {

    TOKEN("TOKEN"),
    HEART_BEAT("心跳"),
    ACK_RECEIVE("消息确认");

    private final String desc;

    private SystemMessageType(String desc) {
        this.desc = desc;
    }

}