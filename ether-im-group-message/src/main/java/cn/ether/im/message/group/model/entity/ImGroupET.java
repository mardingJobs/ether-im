package cn.ether.im.message.group.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户群
 *
 * @TableName im_group
 */
@TableName(value = "im_group")
@Data
public class ImGroupET implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     *
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 群名称
     */
    @TableField(value = "name")
    private String name;
    /**
     * 群备注
     */
    @TableField(value = "remark")
    private String remark;
    /**
     * 群主
     */
    @TableField(value = "owner_id")
    private String ownerId;
    /**
     * 创建人ID
     */
    @TableField(value = "creator_id")
    private String creatorId;
    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;
}