package cn.ether.im.message.single.model.dto;

import cn.ether.im.message.single.model.entity.ImChatMessageEntity;
import lombok.Data;

import java.util.List;

/**
 * * @Author: Martin(微信：martin-jobs)
 * * @Date    2024/9/22 20:21
 * * @Description
 * * @Github <a href="https://github.com/mardingJobs">Github链接</a>
 **/
@Data
public class ChatMessagePullResult {

    private List<ImChatMessageEntity> messages;


    public ChatMessagePullResult(List<ImChatMessageEntity> messages) {
        this.messages = messages;
    }
}
