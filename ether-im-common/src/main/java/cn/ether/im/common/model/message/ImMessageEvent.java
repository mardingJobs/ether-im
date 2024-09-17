package cn.ether.im.common.model.message;

import cn.ether.im.common.enums.ChatMessageType;
import cn.ether.im.common.enums.ImMessageEventType;
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
     * 消息id
     */
    private Long messageId;

    /**
     * 消息类型
     */
    private ChatMessageType messageType;

    /**
     * 用户终端
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

}