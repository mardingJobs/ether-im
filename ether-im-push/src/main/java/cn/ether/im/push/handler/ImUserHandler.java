package cn.ether.im.push.handler;

import cn.ether.im.common.cache.ImUserContextCache;
import cn.ether.im.common.constants.ImConstants;
import cn.ether.im.common.event.event.ImUserLoginEvent;
import cn.ether.im.common.model.user.ImUserTerminal;
import cn.ether.im.push.cache.UserChannelCache;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * * @Author: Martin(微信：martin-jobs)
 * * @Date    2024/10/3 15:55
 * * @Description
 * * @Github <a href="https://github.com/mardingJobs">Github链接</a>
 **/
@Slf4j
@Component
public class ImUserHandler {

    @Resource
    ImUserContextCache contextCache;

    @Resource
    private RocketMQTemplate rocketMQTemplate;


    public void onUserLogin(ImUserTerminal userTerminal, ChannelHandlerContext ctx) {
        // 缓存当前用户终端和连接的push服务
        contextCache.bindPushServer(userTerminal);
        // 将当前用户终端和channel绑定
        UserChannelCache.bindChannel(userTerminal, ctx);
        // 发布用户登陆通知
        ImUserLoginEvent userLoginEvent = new ImUserLoginEvent();
        userLoginEvent.setUserId(userTerminal.getUserId());
        userLoginEvent.setTerminalType(userTerminal.getTerminalType());
        userLoginEvent.setLoginTime(System.currentTimeMillis());
        SendCallback sendCallback = new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                SendStatus sendStatus = sendResult.getSendStatus();
                if (sendStatus == SendStatus.SEND_OK) {
                    log.info("用户登陆通知发送成功,userTerminal:{}", userTerminal);
                } else {
                    log.error("用户登陆通知发送失败,userTerminal:{}", userTerminal);
                }
            }

            @Override
            public void onException(Throwable e) {
                log.error("用户登陆通知发送异常,userTerminal:{}", userTerminal);
            }
        };
        rocketMQTemplate.asyncSend(ImConstants.IM_EVENT_TOPIC, userLoginEvent, sendCallback);
    }

}
