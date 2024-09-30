package cn.ether.im.common.enums;

import cn.ether.im.common.model.info.ImSysAckMessage;
import cn.ether.im.common.model.info.sys.ImHeartbeatMessage;
import cn.ether.im.common.model.info.sys.ImSysMessage;
import cn.ether.im.common.model.info.sys.ImTokenMessage;

/**
 * 系统消息类型
 */
public enum ImSysMessageType {

    TOKEN("TOKEN", ImTokenMessage.class),
    HB("心跳", ImHeartbeatMessage.class),
    ACK("ACK", ImSysAckMessage.class);

    private final String desc;
    private final Class<? extends ImSysMessage> messageClass;

    private ImSysMessageType(String desc, Class messageClass) {
        this.desc = desc;
        this.messageClass = messageClass;
    }

    public String getDesc() {
        return desc;
    }

    public Class<? extends ImSysMessage> getMessageClass() {
        return messageClass;
    }
}