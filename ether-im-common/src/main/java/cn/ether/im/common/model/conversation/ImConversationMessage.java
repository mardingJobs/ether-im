package cn.ether.im.common.model.conversation;

import cn.ether.im.common.model.info.message.ImMessage;
import lombok.Data;

/**
 * * @Author: Martin(微信：martin-jobs)
 * * @Date    2024/9/26 02:59
 * * @Description
 * * @Github <a href="https://github.com/mardingJobs">Github链接</a>
 **/
@Data
public class ImConversationMessage extends ImMessage {

    /**
     * 对话ID
     */
    private Long conversationId;


}
