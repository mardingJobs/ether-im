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

    /**
     * 这里是String类型，防止客户端因为长度太长丢失精度
     */
    private String messageId;

    private ImMessageStatus status;

    private String errorMsg;


    public ImChatMessageSentResult(String messageId, ImMessageStatus status) {
        this.messageId = messageId;
        this.status = status;
    }

    /**
     * 消息发送成功，发到MQ就代表发送成功
     *
     * @param messageId 消息ID
     * @return
     */
    public static ImChatMessageSentResult success(Long messageId) {
        return new ImChatMessageSentResult(String.valueOf(messageId), ImMessageStatus.SENT);
    }

    /**
     * 消息未发送
     *
     * @param messageId
     * @return
     */
    public static ImChatMessageSentResult sentFail(Long messageId, String errorMsg) {
        return new ImChatMessageSentResult(String.valueOf(messageId), ImMessageStatus.INTI, errorMsg);
    }

}
