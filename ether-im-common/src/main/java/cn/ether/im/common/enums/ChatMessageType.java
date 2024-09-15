package cn.ether.im.common.enums;

/**
 * 对话消息类型
 */
public enum ChatMessageType {

    PERSONAL("单聊"),
    GROUP("群聊");

    private final String desc;

    private ChatMessageType(String desc) {
        this.desc = desc;
    }

}