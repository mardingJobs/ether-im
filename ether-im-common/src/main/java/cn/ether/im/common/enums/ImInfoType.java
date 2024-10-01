package cn.ether.im.common.enums;

import cn.ether.im.common.model.info.ImInfo;
import cn.ether.im.common.model.info.MessageReceivedNotice;
import cn.ether.im.common.model.info.message.ImMessage;
import cn.ether.im.common.model.info.sys.ImHeartbeatInfo;

/**
 * 信息类型
 */
public enum ImInfoType {
    INFO(0, "普通信息", ImInfo.class),
    MESSAGE(1, "对话消息", ImMessage.class),
    MESSAGE_RECEIVED(2, "消息已接受", MessageReceivedNotice.class),
    HEART_BEAT(3, "心跳", ImHeartbeatInfo.class),
    PROTOCOL_SELECT_RESULT(4, "协议选择结果", null);

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
}