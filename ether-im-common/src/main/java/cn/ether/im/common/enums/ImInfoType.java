package cn.ether.im.common.enums;

import cn.ether.im.common.model.info.ImInfo;
import cn.ether.im.common.model.info.message.ImMessage;
import cn.ether.im.common.model.info.message.event.ImMessageEvent;
import cn.ether.im.common.model.info.sys.ImSysMessage;

/**
 * 信息类型
 */
public enum ImInfoType {

    MESSAGE("对话消息", ImMessage.class),
    MESSAGE_EVENT("对话消息事件", ImMessageEvent.class),
    SYSTEM("系统", ImSysMessage.class);

    private final String desc;
    private final Class<? extends ImInfo> infoClass;

    private ImInfoType(String desc, Class<? extends ImInfo> infoClass) {
        this.desc = desc;
        this.infoClass = infoClass;
    }

    public Class<? extends ImInfo> getInfoClass() {
        return infoClass;
    }
}