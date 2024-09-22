package cn.ether.im.message.model.entity;

import cn.ether.im.common.enums.ImChatMessageType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * 单聊消息表
 *
 * @TableName im_chat_message
 */
@TableName(value = "im_chat_message")
@Data
@EqualsAndHashCode
@ToString
public class ImChatMessageEntity implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     *
     */
    @TableId
    private Long id;
    /**
     * 消息类型：单聊 群聊
     */
    private ImChatMessageType messageType;
    /**
     * 发送者ID
     */
    private String senderId;

    /**
     * 发送者终端类型:WEB  APP
     */
    private String senderTerminal;
    /**
     * 接收者ID
     */
    private String receiverId;
    /**
     * 消息状态：未发送 已发送 已推送 已触达 已撤回 已读
     */
    private String status;
    /**
     * 消息内容
     */
    private String content;
    /**
     * 消息内容类型: 文字 图片 文件 语音 视频
     */
    private String contentType;
    /**
     * 发送时间
     */
    private Long sendTime;
    /**
     * 创建时间
     */
    private Date createTime;

}