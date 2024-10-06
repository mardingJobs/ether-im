package cn.ether.im.message.single.eventlistener;

import cn.ether.im.common.enums.ImChatMessageStatus;
import cn.ether.im.common.event.event.ImEventType;
import cn.ether.im.common.event.event.impl.ImMessageReceivedEvent;
import cn.ether.im.common.event.listener.ImEventListener;
import cn.ether.im.message.single.model.entity.ImSingleMessageET;
import cn.ether.im.message.single.service.ImSingleMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
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
public class MessageReceivedEventListener implements ImEventListener<ImMessageReceivedEvent> {

    @Resource
    private ImSingleMessageService singleMessageService;

    @Override
    public void onEvent(ImMessageReceivedEvent event) throws Exception {
        log.info("监听【消息已接收事件】|{}", event);
        // 更新状态为已接收
        singleMessageService.lambdaUpdate()
                .eq(ImSingleMessageET::getMessageId, event.getMessageId())
                .eq(ImSingleMessageET::getReceiverId, event.getReceiverId())
                .set(ImSingleMessageET::getStatus, ImChatMessageStatus.RECEIVED.name())
                .update();

    }

    @Override
    public List<ImEventType> listenEventType() {
        return Arrays.asList(ImEventType.MESSAGE_RECEIVED);
    }
}
