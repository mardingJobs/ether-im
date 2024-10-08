package cn.ether.im.common.model.message;

import cn.ether.im.common.enums.ImMessageContentType;
import cn.ether.im.common.enums.ImTerminalType;
import cn.ether.im.common.model.user.ImUserTerminal;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 存放对话消息的公共字段
 * * @Author: Martin(微信：martin-jobs)
 * * @Date    2024/10/5 12:06
 * * @Description
 * * @Github <a href="https://github.com/mardingJobs">Github链接</a>
 **/
@Getter
@Setter
public abstract class ImChatMessage implements ImMessageV2 {


    /**
     * 消息内容
     */
    protected String content;
    /**
     * 消息内容类型
     */
    protected ImMessageContentType contentType;
    /**
     * 发送时间戳
     */
    protected Long sendTime;
    /**
     * 消息ID
     */
    private Long messageId;
    /**
     * 发送者 ID
     */
    private String senderId;
    /**
     * 发送者终端类型
     */
    private ImTerminalType senderTerminal;

    /**
     * 用于限制接受终端，如果为空，则不用限制。
     */
    private List<ImUserTerminal> limitTerminals;

}
