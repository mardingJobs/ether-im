package cn.ether.im.message.single.eventlistener;

import cn.ether.im.common.event.event.ImEventType;
import cn.ether.im.common.event.event.impl.ImMessageReceivedEvent;
import cn.ether.im.common.event.listener.ImEventListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * * @Author: Martin(微信：martin-jobs)
 * * @Date    2024/10/4 02:20
 * * @Description
 * * @Github <a href="https://github.com/mardingJobs">Github链接</a>
 **/
@Slf4j
@Component
public class MessageReceivedListener implements ImEventListener<ImMessageReceivedEvent> {


    @Override
    public void onEvent(ImMessageReceivedEvent event) throws Exception {
        log.info("监听【消息已接收事件】|{}", event);
    }

    @Override
    public List<ImEventType> listenEventType() {
        return Arrays.asList(ImEventType.MESSAGE_RECEIVED);
    }
}
