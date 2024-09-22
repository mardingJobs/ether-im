package cn.ether.im.message.model.dto;

import cn.ether.im.common.enums.ImChatMessageType;
import cn.ether.im.common.enums.MessageContentType;
import lombok.Data;

/**
 * * @Author: Martin(微信：martin-jobs)
 * * @Date    2024/9/22 20:24
 * * @Description
 * * @Github <a href="https://github.com/mardingJobs">Github链接</a>
 **/
@Data
public class ImChatMessageVo {

    /**
     * 消息内容
     */
    protected String content;
    /**
     * 消息内容类型
     */
    protected MessageContentType contentType;
    /**
     * 发送时间戳
     */
    protected Long sendTime;
    /**
     * 发送者ID
     */
    private String senderId;
    /**
     * 消息类型
     */
    private ImChatMessageType type;
    /**
     * 消息ID
     */
    private Long id;


}
