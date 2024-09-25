package cn.ether.im.common.model.conversation;

import cn.ether.im.common.model.message.ImChatMessage;
import lombok.Data;

/**
 * * @Author: Martin(微信：martin-jobs)
 * * @Date    2024/9/26 02:59
 * * @Description
 * * @Github <a href="https://github.com/mardingJobs">Github链接</a>
 **/
@Data
public class ImConversationMessage extends ImChatMessage {

    /**
     * 对话ID
     */
    private Long conversationId;


}
