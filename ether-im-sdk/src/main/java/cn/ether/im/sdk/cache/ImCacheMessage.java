package cn.ether.im.sdk.cache;

import cn.ether.im.common.enums.ImMessageType;
import lombok.Data;

/**
 * * @Author: Martin(微信：martin-jobs)
 * * @Date    2024/9/29 21:01
 * * @Description
 * * @Github <a href="https://github.com/mardingJobs">Github链接</a>
 **/
@Data
public class ImCacheMessage {

    private static final long serialVersionUID = 1L;
    /**
     * 发送者ID
     */
    protected String senderId;
    /**
     * 消息内容
     */
    protected String content;
    /**
     * 消息内容类型
     */
    protected String contentType;
    /**
     * 发送时间戳
     */
    protected Long sendTime;
    /**
     * 消息ID
     */
    private Long id;
    /**
     * 消息类型
     */
    private ImMessageType chatMessageType;
    private String sendTerminal;
    private String receiverId;

}
