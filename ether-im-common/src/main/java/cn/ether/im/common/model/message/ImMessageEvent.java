package cn.ether.im.common.model.message;

import cn.ether.im.common.enums.ImChatMessageType;
import cn.ether.im.common.enums.ImSystemMessageType;
import cn.ether.im.common.event.ImMessageEventType;
import cn.ether.im.common.model.user.ImUserTerminal;
import lombok.Data;

/**
 * * @Author: Martin
 * * @Date    2024/9/16 02:07
 * * @Description
 **/
@Data
public class ImMessageEvent extends ImSystemMessage {

    /**
     * 原始消息id
     */
    private Long messageId;

    /**
     * 消息类型
     */
    private ImChatMessageType chatMessageType;

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

    public ImMessageEvent(ImSystemMessageType systemMessageType) {
        this.setSystemMessageType(ImSystemMessageType.EVENT);
    }

    /**
     * 获取唯一标识
     *
     * @return
     */
    @Override
    public String uid() {
        return String.valueOf(this.getMessageId());
    }
}
