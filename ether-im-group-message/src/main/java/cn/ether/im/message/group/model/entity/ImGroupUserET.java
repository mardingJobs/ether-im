package cn.ether.im.message.group.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 群和用户关系
 *
 * @TableName im_group_user
 */
@TableName(value = "im_group_user")
@Data
public class ImGroupUserET implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 群ID
     */
    @TableField(value = "group_id")
    private String groupId;
    /**
     * 用户ID
     */
    @TableField(value = "user_id")
    private String userId;
}