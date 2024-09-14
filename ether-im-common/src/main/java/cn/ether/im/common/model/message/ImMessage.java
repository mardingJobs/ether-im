package cn.ether.im.common.model.message;

import cn.ether.im.common.enums.ImMessageType;
import cn.ether.im.common.model.ImUser;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * * @Author: Martin
 * * @Date    2024/9/14 18:17
 * * @Description
 **/
@Data
public class ImMessage {

    /**
     * 消息ID
     */
    private String id;

    /**
     * 发送者
     */
    protected ImUser sender;

    /**
     * 接收者
     */
    protected List<ImUser> receivers = new LinkedList<>();

    /**
     * 消息类型
     */
    private ImMessageType type;

    /**
     * 消息内容
     */
    protected String content;


    /**
     * 时间戳
     */
    private Long timestamp;

}
