package cn.ether.im.message.single.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @TableName im_group_user
 */
@TableName(value = "im_group_user")
@Data
public class ImGroupUser implements Serializable {

    private static final long serialVersionUID = 1L;
    private String groupId;
    private String userId;
}