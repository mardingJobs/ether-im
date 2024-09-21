package cn.ether.im.message.model.dto;

import lombok.Data;

/**
 * * @Author: Martin
 * * @Date    2024/9/15 15:00
 * * @Description
 **/
@Data
public class PersonalChatMessageReq {


    private static final long serialVersionUID = 1L;
    /**
     * 发送者ID
     */
    private String senderId;
    /**
     * 发送者所属的组
     */
    private String senderGroup;
    /**
     * 发送者终端类型:WEB  APP
     */
    private String senderTerminal;
    /**
     * 接收者ID
     */
    private String receiverId;
    /**
     * 接收者所属的组
     */
    private String receiverGroup;
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
