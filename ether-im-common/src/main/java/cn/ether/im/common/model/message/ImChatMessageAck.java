package cn.ether.im.common.model.message;

import lombok.Data;

/**
 * * @Author: Martin
 * * @Date    2024/9/16 02:07
 * * @Description
 **/
@Data
public class ImChatMessageAck extends ImSystemMessage {

    /**
     * 消息id
     */
    private String messageId;

    /**
     * 接收者id
     */
    private String receiverId;

}
