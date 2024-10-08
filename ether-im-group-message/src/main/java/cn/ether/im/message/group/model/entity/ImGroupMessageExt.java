package cn.ether.im.message.group.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 存放未接收的消息
 *
 * @TableName im_group_message_ext
 */
@TableName(value = "im_group_message_ext")
@Data
public class ImGroupMessageExt implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 消息ID
     */
    @TableId
    private Long messageId;
    /**
     * 群ID
     */
    @TableField(value = "group_id")
    private Long groupId;
    /**
     * 已接收消息的用户ID列表
     */
    @TableField(value = "received_user_ids")
    private String receivedUserIds;
}