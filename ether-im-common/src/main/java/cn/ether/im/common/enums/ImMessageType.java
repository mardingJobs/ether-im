package cn.ether.im.common.enums;

/**
 * 对话消息类型
 */
public enum ImMessageType {

    PERSONAL("单聊"),
    GROUP("群聊");


    private final String desc;

    ImMessageType(String desc) {
        this.desc = desc;
    }

}