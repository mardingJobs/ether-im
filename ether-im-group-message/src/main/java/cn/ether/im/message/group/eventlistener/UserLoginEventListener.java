package cn.ether.im.message.group.eventlistener;

import cn.ether.im.common.event.event.ImEventType;
import cn.ether.im.common.event.event.impl.ImUserLoginEvent;
import cn.ether.im.common.event.listener.ImEventListener;
import cn.ether.im.message.group.service.ImGroupMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * * @Author: Martin(微信：martin-jobs)
 * * @Date    2024/10/6 18:13
 * * @Description
 * * @Github <a href="https://github.com/mardingJobs">Github链接</a>
 **/
@Slf4j
@Component
public class UserLoginEventListener implements ImEventListener<ImUserLoginEvent> {

    @Resource
    private ImGroupMessageService groupMessageService;

    @Override
    public void onEvent(ImUserLoginEvent event) {
        String userId = event.getUserId();
        groupMessageService.resendUnReceivedMessage(userId, event.getTerminalType());
    }

    @Override
    public List<ImEventType> listenEventType() {
        return Arrays.asList(ImEventType.USER_LOGIN);
    }
}
