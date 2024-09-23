package cn.ether.im.push.processor.chat;

/**
 * * @Author: Martin(微信：martin-jobs)
 * * @Date    2024/9/24 00:21
 * * @Description
 * * @Github <a href="https://github.com/mardingJobs">Github链接</a>
 **/

import cn.ether.im.common.enums.ImTerminalType;
import cn.ether.im.common.event.ImMessageEventType;
import cn.ether.im.common.event.listener.ImEventListener;
import cn.ether.im.common.event.listener.ImMessageEventListener;
import cn.ether.im.common.exception.RetryException;
import cn.ether.im.common.model.message.ImChatMessage;
import cn.ether.im.common.model.message.ImMessageEvent;
import cn.ether.im.common.model.user.ImUserTerminal;
import cn.ether.im.push.cache.UserChannelCache;
import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

@Slf4j
@ImEventListener(listenEventTypes = ImMessageEventType.REACHED)
@Component
public class RetryableMessageFlusher implements ImMessageEventListener {

    private static Cache<String, String> REACHED_MESSAGES = CacheBuilder.newBuilder().
            maximumSize(10).build();

    @Retryable(value = RetryException.class, maxAttempts = 3, backoff = @Backoff(delay = 2000, multiplier = 2))
    public void flush(ImUserTerminal receiverTerminal, ImChatMessage message) {
        String cacheKey = cacheKey(message.getId(), receiverTerminal.getUserId(), receiverTerminal.getTerminalType());
        if (REACHED_MESSAGES.asMap().containsKey(cacheKey)) {
            log.info("flush|消息已触达，无需重发");
            return;
        }
        ChannelHandlerContext ctx = UserChannelCache.getChannelCtx(receiverTerminal.getUserId(),
                receiverTerminal.getTerminalType().toString());
        if (ctx != null) {
            ImChatMessage copiedMessage = BeanUtil.copyProperties(message, ImChatMessage.class, "receiverTerminals", "receivers");
            copiedMessage.setReceivers(null);
            copiedMessage.setReceiverTerminals(null);
            ctx.writeAndFlush(copiedMessage);
            log.info("flush|消息已推送，等待确认。Message:{}", JSON.toJSONString(copiedMessage));
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                log.info("flush|Thread sleep Interrupted");
            }
            if (!REACHED_MESSAGES.asMap().containsKey(cacheKey)) {
                throw new RetryException();
            }
        }
    }


    @Override
    public void onMessageEvent(ImMessageEvent messageEvent) throws Exception {
        log.info("监听到消息事件｜{}", JSON.toJSONString(messageEvent));
        ImUserTerminal terminal = messageEvent.getTerminal();
        String cacheKey = cacheKey(messageEvent.getMessageId(), terminal.getUserId(), terminal.getTerminalType());
        REACHED_MESSAGES.put(cacheKey, "");
    }

    private String cacheKey(Long messageId, String userId, ImTerminalType terminalType) {
        return messageId + ":" + userId + ":" + terminalType.name();
    }
}
