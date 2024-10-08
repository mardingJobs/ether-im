package cn.ether.im.common.model.message;

import cn.ether.im.common.enums.ImMessageType;
import lombok.Data;

import java.util.List;

/**
 * * @Author: Martin(微信：martin-jobs)
 * * @Date    2024/10/4 21:58
 * * @Description
 * * @Github <a href="https://github.com/mardingJobs">Github链接</a>
 **/
@Data
public class ImGroupMessage extends ImChatMessage {

    private static final long serialVersionUID = 1L;

    /**
     * 群组ID
     */
    private String groupId;

    /**
     * 接收者
     */
    private List<String> receiverIds;

    @Override
    public Long messageId() {
        return this.getMessageId();
    }

    @Override
    public ImMessageType messageType() {
        return ImMessageType.GROUP;
    }
}
