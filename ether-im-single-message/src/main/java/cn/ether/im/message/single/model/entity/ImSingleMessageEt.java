package cn.ether.im.message.single.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @TableName im_single_message
 */
@TableName(value = "im_single_message")
@Data
public class ImSingleMessageEt implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String senderId;
    private String senderTerminal;
    private String receiverId;
    private String content;
    private String contentType;
    private String status;
    private Long sendTime;
    private Date createTime;
}