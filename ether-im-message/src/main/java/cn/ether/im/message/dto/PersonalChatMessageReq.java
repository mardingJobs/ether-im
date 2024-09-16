package cn.ether.im.message.dto;

import cn.ether.im.common.enums.MessageContentType;
import cn.ether.im.common.model.user.ImUser;
import cn.ether.im.common.model.user.ImUserTerminal;
import lombok.Data;

/**
 * * @Author: Martin
 * * @Date    2024/9/15 15:00
 * * @Description
 **/
@Data
public class PersonalChatMessageReq {


    /**
     * 发送者
     */
    protected ImUserTerminal sender;

    /**
     * 接收者
     */
    protected ImUser receiver;

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
