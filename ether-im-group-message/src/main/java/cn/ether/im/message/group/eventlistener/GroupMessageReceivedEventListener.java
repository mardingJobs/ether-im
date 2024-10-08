package cn.ether.im.message.group.eventlistener;

import cn.ether.im.common.event.event.ImEventType;
import cn.ether.im.common.event.event.impl.ImGroupMessageReceivedEvent;
import cn.ether.im.common.event.listener.ImEventListener;
import cn.ether.im.message.group.service.ImGroupMessageExtService;
import cn.ether.im.message.group.service.ImGroupMessageService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
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
public class GroupMessageReceivedEventListener implements ImEventListener<ImGroupMessageReceivedEvent> {

    @Resource
    private ImGroupMessageService groupMessageService;

    @Resource
    private ImGroupMessageExtService groupMessageExtService;

    @Resource
    private RedissonClient redissonClient;

    @Override
    public void onEvent(ImGroupMessageReceivedEvent event) throws Exception {
        log.info("监听【消息已接收事件】|{}", event);
        // 更新状态为已接收
        RLock lock = redissonClient.getLock("group-message-receive-lock-" + event.getMessageId());
        lock.lock();
        try {
            groupMessageExtService.refreshReceiveIds(event.getMessageId(), event.getReceiverId());
        } catch (Exception e) {
            log.error("更新已接收用户列表失败", e);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public List<ImEventType> listenEventType() {
        return Arrays.asList(ImEventType.GROUP_MESSAGE_RECEIVED);
    }
}
