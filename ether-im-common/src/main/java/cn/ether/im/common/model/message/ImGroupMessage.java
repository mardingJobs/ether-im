package cn.ether.im.common.model.message;

import cn.ether.im.common.enums.ImMessageContentType;
import cn.ether.im.common.enums.ImMessageType;
import cn.ether.im.common.enums.ImTerminalType;
import lombok.Data;

import java.util.List;

/**
 * * @Author: Martin(微信：martin-jobs)
 * * @Date    2024/10/4 21:58
 * * @Description
 * * @Github <a href="https://github.com/mardingJobs">Github链接</a>
 **/
@Data
public class ImGroupMessage implements ImMessageV2 {

    private static final long serialVersionUID = 1L;
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
     * 发送终端类型
     */
    private ImTerminalType senderTerminal;
    /**
     * 接收者
     */
    private List<String> receiverId;

    @Override
    public ImMessageType getMessageType() {
        return ImMessageType.GROUP;
    }
}
