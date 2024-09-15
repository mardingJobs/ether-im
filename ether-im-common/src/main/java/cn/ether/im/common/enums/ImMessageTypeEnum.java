package cn.ether.im.common.enums;

/**
 * 消息类型
 */
public enum ImMessageTypeEnum {

    CHAT("对话"),
    SYSTEM("系统");

    private final String desc;

    private ImMessageTypeEnum(String desc) {
        this.desc = desc;
    }

}