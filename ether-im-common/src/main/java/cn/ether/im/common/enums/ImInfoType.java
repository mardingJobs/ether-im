package cn.ether.im.common.enums;

import cn.ether.im.common.model.info.ImInfo;
import cn.ether.im.common.model.info.message.ImMessage;
import cn.ether.im.common.model.info.message.ImMessageReceived;

/**
 * 信息类型
 */
public enum ImInfoType {

    HEART_BEAT(1, "心跳", ImInfo.class),
    INFO(2, "普通信息", ImInfo.class),
    MESSAGE(3, "对话消息", ImMessage.class),
    MESSAGE_RECEIVED(4, "消息已接受", ImMessageReceived.class),
    MESSAGE_READ_NOTICE(5, "消息已读通知", ImMessage.class),
    MESSAGE_WITH_DRAWN_NOTICE(6, "消息已撤回通知", ImMessage.class);

    private final int code;
    private final String desc;
    private final Class<? extends ImInfo> infoClass;

    private ImInfoType(int code, String desc, Class<? extends ImInfo> infoClass) {
        this.code = code;
        this.desc = desc;
        this.infoClass = infoClass;
    }

    public Class<? extends ImInfo> getInfoClass() {
        return infoClass;
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