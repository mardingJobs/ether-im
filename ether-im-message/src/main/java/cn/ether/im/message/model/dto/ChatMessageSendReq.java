package cn.ether.im.message.model.dto;

import cn.ether.im.common.enums.ImChatMessageType;
import lombok.Data;

/**
 * * @Author: Martin
 * * @Date    2024/9/15 15:00
 * * @Description
 **/
@Data
public class ChatMessageSendReq {


    private static final long serialVersionUID = 1L;
    /**
     * 发送者ID
     */
    private String senderId;

    /**
     * 发送者终端类型:WEB  APP
     */
    private String senderTerminal;
    /**
     * 接收者ID
     */
    private String receiverId;

    /**
     * 消息类型: 单聊 群聊
     */
    private ImChatMessageType messageType;
    /**
     * 消息内容
     */
    private String content;
    /**
     * 消息内容类型: 文字 图片 文件 语音 视频
     */
    private String contentType;
    /**
     * 时间戳 (毫秒)
     */
    private Long sendTime;

}
