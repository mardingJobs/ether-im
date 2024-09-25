package cn.ether.im.common.model.conversation;

import cn.ether.im.common.enums.ImChatMessageContentType;
import lombok.Data;

import java.util.Date;

/**
 * * @Author: Martin(微信：martin-jobs)
 * * @Date    2024/9/26 02:45
 * * @Description
 * * @Github <a href="https://github.com/mardingJobs">Github链接</a>
 **/
@Data
public class ImConversation {

    /**
     * 会话id
     */
    private Long conversationId;

    /**
     * 最后一条消息id
     */
    private Long lastMessageId;

    /**
     * 最后一条消息内容
     */
    private String lastMessageContent;

    /**
     * 最后一条消息类型
     */
    private ImChatMessageContentType lastMessageContentType;

    /**
     * 更新时间
     */
    private Date updatedTime;

    /**
     * 创建时间
     */
    private Date createTime;

}
