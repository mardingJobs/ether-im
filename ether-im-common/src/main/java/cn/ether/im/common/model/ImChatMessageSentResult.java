package cn.ether.im.common.model;

import cn.ether.im.common.enums.ImMessageStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 表示消息发送结果
 * * @Author: Martin
 * * @Date    2024/9/17 15:16
 * * @Description
 **/
@Data
@AllArgsConstructor
public class ImChatMessageSentResult {

    private Long messageId;

    private ImMessageStatus status;

    /**
     * 消息发送成功，发到MQ就代表发送成功
     *
     * @param messageId 消息ID
     * @return
     */
    public static ImChatMessageSentResult success(Long messageId) {
        return new ImChatMessageSentResult(messageId, ImMessageStatus.SENT);
    }

    /**
     * 消息发送失败
     *
     * @param messageId
     * @return
     */
    public static ImChatMessageSentResult fail(Long messageId) {
        return new ImChatMessageSentResult(messageId, ImMessageStatus.UN_SEND);
    }

}
