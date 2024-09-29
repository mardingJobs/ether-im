package cn.ether.im.common.model.info.message.event;

import cn.ether.im.common.enums.ImMessageType;
import cn.ether.im.common.model.info.ImInfo;
import cn.ether.im.common.model.user.ImUserTerminal;
import lombok.Data;

/**
 * * @Author: Martin
 * * @Date    2024/9/16 02:07
 * * @Description
 **/
@Data
public class ImMessageEvent extends ImInfo {

    /**
     * 原始消息id
     */
    private Long messageId;

    /**
     * 消息类型
     */
    private ImMessageType chatMessageType;

    /**
     * 触发事件的用户终端
     */
    private ImUserTerminal terminal;

    /**
     * 事件类型
     */
    private ImMessageEventType eventType;

    /**
     * 事件发生时间
     */
    private Long eventTime;

    public ImMessageEvent() {
    }
}
