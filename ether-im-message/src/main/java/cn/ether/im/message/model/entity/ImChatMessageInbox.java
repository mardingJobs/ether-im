package cn.ether.im.message.model.entity;

import cn.ether.im.common.enums.ImMessageType;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @TableName im_chat_message_inbox
 */
@TableName(value = "im_chat_message_inbox")
@Data
public class ImChatMessageInbox implements Serializable {

    private static final long serialVersionUID = 1L;
    private String receiverId;
    private String senderId;
    private Long messageId;
    private ImMessageType messageType;
    private Long sendTime;
}