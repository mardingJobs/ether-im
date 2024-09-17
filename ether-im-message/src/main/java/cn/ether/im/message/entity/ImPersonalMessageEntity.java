package cn.ether.im.message.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 单聊消息表
 *
 * @TableName im_personal_message
 */
@TableName(value = "im_personal_message")
@Data
public class ImPersonalMessageEntity implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     *
     */
    @TableId
    private Long id;
    /**
     * 发送者ID
     */
    private String senderId;
    /**
     * 发送者所属的组
     */
    private String senderGroup;
    /**
     * 发送者终端类型:WEB  APP
     */
    private String senderTerminal;
    /**
     * 接收者ID
     */
    private String receiverId;
    /**
     * 接收者所属的组
     */
    private String receiverGroup;
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

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        ImPersonalMessageEntity other = (ImPersonalMessageEntity) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getSenderId() == null ? other.getSenderId() == null : this.getSenderId().equals(other.getSenderId()))
                && (this.getSenderGroup() == null ? other.getSenderGroup() == null : this.getSenderGroup().equals(other.getSenderGroup()))
                && (this.getSenderTerminal() == null ? other.getSenderTerminal() == null : this.getSenderTerminal().equals(other.getSenderTerminal()))
                && (this.getReceiverId() == null ? other.getReceiverId() == null : this.getReceiverId().equals(other.getReceiverId()))
                && (this.getReceiverGroup() == null ? other.getReceiverGroup() == null : this.getReceiverGroup().equals(other.getReceiverGroup()))
                && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
                && (this.getContent() == null ? other.getContent() == null : this.getContent().equals(other.getContent()))
                && (this.getContentType() == null ? other.getContentType() == null : this.getContentType().equals(other.getContentType()))
                && (this.getSendTime() == null ? other.getSendTime() == null : this.getSendTime().equals(other.getSendTime()))
                && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getSenderId() == null) ? 0 : getSenderId().hashCode());
        result = prime * result + ((getSenderGroup() == null) ? 0 : getSenderGroup().hashCode());
        result = prime * result + ((getSenderTerminal() == null) ? 0 : getSenderTerminal().hashCode());
        result = prime * result + ((getReceiverId() == null) ? 0 : getReceiverId().hashCode());
        result = prime * result + ((getReceiverGroup() == null) ? 0 : getReceiverGroup().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getContent() == null) ? 0 : getContent().hashCode());
        result = prime * result + ((getContentType() == null) ? 0 : getContentType().hashCode());
        result = prime * result + ((getSendTime() == null) ? 0 : getSendTime().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", senderId=").append(senderId);
        sb.append(", senderGroup=").append(senderGroup);
        sb.append(", senderTerminal=").append(senderTerminal);
        sb.append(", receiverId=").append(receiverId);
        sb.append(", receiverGroup=").append(receiverGroup);
        sb.append(", status=").append(status);
        sb.append(", content=").append(content);
        sb.append(", contentType=").append(contentType);
        sb.append(", sendTime=").append(sendTime);
        sb.append(", createTime=").append(createTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}