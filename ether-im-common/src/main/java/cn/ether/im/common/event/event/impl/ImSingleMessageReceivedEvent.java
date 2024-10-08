package cn.ether.im.common.event.event.impl;

import cn.ether.im.common.event.event.ImEvent;
import cn.ether.im.common.event.event.ImEventType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 消息已被用户接受，任意终端接受都会触发事件
 * * @Author: Martin(微信：martin-jobs)
 * * @Date    2024/10/3 21:43
 * * @Description
 * * @Github <a href="https://github.com/mardingJobs">Github链接</a>
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImSingleMessageReceivedEvent implements ImEvent {

    private static final long serialVersionUID = 1L;

    private Long messageId;

    private String receiverId;


    @Override
    public ImEventType getEventType() {
        return ImEventType.SINGLE_MESSAGE_RECEIVED;
    }
}
