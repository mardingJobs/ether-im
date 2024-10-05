package cn.ether.im.common.event.event.impl;

import cn.ether.im.common.event.event.ImEvent;
import cn.ether.im.common.event.event.ImEventType;
import cn.ether.im.common.model.message.ImChatMessage;
import lombok.Getter;
import lombok.Setter;

/**
 * 当消息发送到MQ时触发该事件
 * * @Author: Martin(微信：martin-jobs)
 * * @Date    2024/10/5 18:03
 * * @Description
 * * @Github <a href="https://github.com/mardingJobs">Github链接</a>
 **/
@Getter
@Setter
public class ImMessageSentToMQEvent implements ImEvent {

    private ImChatMessage chatMessage;

    @Override
    public ImEventType getEventType() {
        return ImEventType.MESSAGE_SENT_TO_MQ;
    }
}
