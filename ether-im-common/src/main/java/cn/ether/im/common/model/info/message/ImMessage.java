package cn.ether.im.common.model.info.message;

import cn.ether.im.common.enums.ImInfoType;
import cn.ether.im.common.enums.ImMessageContentType;
import cn.ether.im.common.enums.ImMessageType;
import cn.ether.im.common.model.info.ImInfo;
import cn.ether.im.common.model.user.ImUser;
import cn.ether.im.common.model.user.ImUserTerminal;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 *  对话消息
 * * @Author: Martin
 * * @Date    2024/9/14 18:17
 * * @Description
 **/
@Data
public class ImMessage extends ImInfo {

    private static final long serialVersionUID = 1L;

    /**
     * 发送者
     */
    protected ImUserTerminal sender;

    /**
     * 接收者
     * 如果是单聊，接收者是用户ID，只有1个；如果是群聊，接收者是群ID
     */
    protected List<ImUser> receivers = new LinkedList<>();

    /**
     * 接收者终端
     * 在发送消息时，不用指定，SDK会自动选择
     * 推送服务根据这些终端来推送消息
     */
    protected List<ImUserTerminal> receiverTerminals = new LinkedList<>();

    /**
     * 消息内容
     */
    protected String content;

    /**
     * 消息内容类型
     */
    protected ImMessageContentType contentType;

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
    private ImMessageType chatMessageType;

    public ImMessage() {
        this.setType(ImInfoType.MESSAGE);
    }

    /**
     * 获取唯一标识
     *
     * @return
     */
    @Override
    public String uid() {
        return String.valueOf(id);
    }


}
