package cn.ether.im.message.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @TableName im_conversation
 */
@TableName(value = "im_conversation")
@Data
public class ImConversationEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;
    private String creatorId;
    private Long lastMessageId;
    private String lastMessageContent;
    private String lastMessageContentType;
    private Date updateTime;
    private Date createTime;
}