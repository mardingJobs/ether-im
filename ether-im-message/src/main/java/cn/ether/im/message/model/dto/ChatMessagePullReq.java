package cn.ether.im.message.model.dto;

import cn.ether.im.common.enums.ImChatMessageType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 离线拉取消息
 * * @Author: Martin(微信：martin-jobs)
 * * @Date    2024/9/22 02:10
 * * @Description
 * * @Github <a href="https://github.com/mardingJobs">Github链接</a>
 **/
@Data
public class ChatMessagePullReq {

    private String userId;

    private String senderId;

    private String receiverId;

    @Schema(description = "消息类型", example = "PERSONAL｜GROUP")
    private ImChatMessageType messageType;

    @Schema(description = "最小消息ID", example = "1837884110291472384")
    private Long minMessageId;

}