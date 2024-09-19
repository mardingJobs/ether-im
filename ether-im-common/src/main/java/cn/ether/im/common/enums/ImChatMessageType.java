package cn.ether.im.common.enums;

/**
 * 对话消息类型
 */
public enum ImChatMessageType {

    PERSONAL("单聊"),
    GROUP("群聊"),
    READ("已读"),
    WITH_DRAWN("已撤回");

    private final String desc;

    ImChatMessageType(String desc) {
        this.desc = desc;
    }

}