package cn.ether.im.common.enums;

/**
 * 终端和push端交互使用的消息类型
 */
public enum ImMessageType {

    TOKEN(0, "TOKEN"),
    HEART_BEAT(1, "心跳"),
    OFFLINE(2, "下线"),
    PERSONAL(3, "单聊"),
    GROUP(4, "群发");

    private final Integer code;
    private final String desc;

    private ImMessageType(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static ImMessageType fromCode(Integer code) {
        ImMessageType[] var1 = values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            ImMessageType typeEnum = var1[var3];
            if (typeEnum.code.equals(code)) {
                return typeEnum;
            }
        }

        return null;
    }

    public Integer code() {
        return this.code;
    }
}