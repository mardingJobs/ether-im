package cn.ether.im.client.common.enums;


/**
 * 信息类型
 */
public enum ImInfoType {

    HEART_BEAT(1, "心跳"),
    COMMON(2, "通用信息"),
    MESSAGE(3, "对话消息"),
    MESSAGE_RECEIVED(4, "消息已接受"),
    MESSAGE_READ_NOTICE(5, "消息已读通知"),
    MESSAGE_WITH_DRAWN_NOTICE(6, "消息已撤回通知");

    private final int code;
    private final String desc;

    private ImInfoType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }


    public static ImInfoType getByCode(int code) {
        for (ImInfoType type : values()) {
            if (type.getCode() == code) {
                return type;
            }
        }
        return null;
    }

    public int getCode() {
        return code;
    }

}