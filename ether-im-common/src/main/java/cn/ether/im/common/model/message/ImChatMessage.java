package cn.ether.im.common.model.message;

import cn.ether.im.common.enums.ChatMessageType;
import cn.ether.im.common.enums.MessageContentType;
import cn.ether.im.common.model.user.ImUser;
import cn.ether.im.common.model.user.ImUserTerminal;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * * @Author: Martin
 * * @Date    2024/9/14 18:17
 * * @Description
 **/
@Data
public class ImChatMessage extends ImMessage {

    private static final long serialVersionUID = 1L;

    /**
     * 发送者
     */
    protected ImUserTerminal sender;

    /**
     * 接收者
     */
    protected List<ImUser> receivers = new LinkedList<>();

    /**
     * 接收者终端
     */
    protected List<ImUserTerminal> receiverTerminals = new LinkedList<>();

    /**
     * 消息内容
     */
    protected String content;

    /**
     * 消息内容类型
     */
    protected MessageContentType contentType;

    /**
     * 发送时间戳
     */
    protected Long sendTime;

    /**
     * 消息ID
     */
    private Long id;

    /**
     * 消息类型
     */
    private ChatMessageType type;

    /**
     * 获取唯一标识
     *
     * @return
     */
    @Override
    public String getUniqueId() {
        return String.valueOf(id);
    }
}
