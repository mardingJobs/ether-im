package cn.ether.im.proto.text;

import lombok.Data;

/**
 * * @Author: Martin(微信：martin-jobs)
 * * @Date    2024/10/2 14:43
 * * @Description
 * * @Github <a href="https://github.com/mardingJobs">Github链接</a>
 **/
@Data
public class ImMessageTextProto {

    /**
     * 消息ID
     */
    private Long messageId;

    /**
     * 消息发送者ID
     */
    private String senderId;

    /**
     * 发送者终端类型
     */
    private Integer senderTerminal;

    /**
     * 消息类型
     */
    private Integer messageType;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 消息内容类型
     */
    private Integer contentType;

    /**
     * 消息发送时间-毫秒
     */
    private Long sendTime;


}
