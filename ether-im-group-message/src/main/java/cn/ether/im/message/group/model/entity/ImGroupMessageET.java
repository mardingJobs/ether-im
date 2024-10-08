package cn.ether.im.message.group.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 群聊消息表，存放所有群消息
 *
 * @TableName im_group_message
 */
@TableName(value = "im_group_message")
@Data
public class ImGroupMessageET implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     *
     */
    @TableId(value = "message_id")
    private Long messageId;
    /**
     * 会话ID
     */
    @TableField(value = "conversation_id")
    private Long conversationId;
    /**
     * 发送者ID
     */
    @TableField(value = "sender_id")
    private String senderId;
    /**
     * 发送者终端类型:WEB  APP
     */
    @TableField(value = "sender_terminal")
    private String senderTerminal;
    /**
     * 群ID
     */
    @TableField(value = "group_id")
    private Long groupId;
    /**
     * 消息状态：未发送 已发送 已触达 已撤回 已读
     */
    @TableField(value = "status")
    private String status;
    /**
     * 消息内容
     */
    @TableField(value = "content")
    private String content;
    /**
     * 消息内容类型: 文字 图片 文件 语音 视频
     */
    @TableField(value = "content_type")
    private String contentType;
    /**
     * 发送时间
     */
    @TableField(value = "send_time")
    private Long sendTime;
    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;
}