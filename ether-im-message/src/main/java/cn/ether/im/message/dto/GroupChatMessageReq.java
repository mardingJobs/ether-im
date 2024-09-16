package cn.ether.im.message.dto;

import cn.ether.im.common.enums.MessageContentType;
import cn.ether.im.common.model.user.ImUser;
import lombok.Data;

import java.util.List;

/**
 * * @Author: Martin
 * * @Date    2024/9/16 22:56
 * * @Description
 **/
@Data
public class GroupChatMessageReq {


    /**
     * 发送者
     */
    protected ImUser sender;

    /**
     * 接收者列表
     */
    protected List<ImUser> receivers;

    /**
     * 消息内容
     */
    protected String content;

    /**
     * 消息内容类型
     */
    private MessageContentType contentType;

    /**
     * 时间戳
     */
    private Long timestamp;


}
