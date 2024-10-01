package cn.ether.im.push.processor.flusher;

/**
 * * @Author: Martin(微信：martin-jobs)
 * * @Date    2024/9/24 00:21
 * * @Description
 * * @Github <a href="https://github.com/mardingJobs">Github链接</a>
 **/

import cn.ether.im.common.enums.ImInfoType;
import cn.ether.im.common.enums.ImTerminalType;
import cn.ether.im.common.exception.RetryException;
import cn.ether.im.common.model.info.MessageReceivedNotice;
import cn.ether.im.common.model.info.message.ImMessage;
import cn.ether.im.common.model.user.ImUserTerminal;
import cn.ether.im.push.cache.UserChannelCache;
import cn.ether.im.push.processor.ImInfoProcessor;
import cn.hutool.core.bean.BeanUtil;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RetryableMessageFlusher extends ImInfoProcessor<MessageReceivedNotice> implements ImMessageFlusher {

    private static Cache<String, String> REACHED_MESSAGES = CacheBuilder.newBuilder()
            .initialCapacity(10)
            .concurrencyLevel(8)
            .expireAfterWrite(60, java.util.concurrent.TimeUnit.SECONDS)
            .maximumSize(100).build();

    @Retryable(value = RetryException.class, maxAttempts = 3, backoff = @Backoff(delay = 2000, multiplier = 2))
    @Override
    public void flush(ImUserTerminal receiverTerminal, ImMessage message) {
        String cacheKey = cacheKey(message.getId(), receiverTerminal.getUserId(), receiverTerminal.getTerminalType());
        if (REACHED_MESSAGES.asMap().containsKey(cacheKey)) {
            log.info("消息已触达,MessageId:{},Terminal:{}", message.getId(), receiverTerminal);
            return;
        }
        ChannelHandlerContext ctx = UserChannelCache.getChannelCtx(receiverTerminal.getUserId(),
                receiverTerminal.getTerminalType().toString());
        if (ctx != null) {
            ImMessage copiedMessage = BeanUtil.copyProperties(message, ImMessage.class, "receiverTerminals", "receivers");
            copiedMessage.setReceivers(null);
            copiedMessage.setReceiverTerminals(null);
            ctx.writeAndFlush(copiedMessage);
            if (log.isDebugEnabled()) {
                log.debug("flush|消息已推送，等待确认。MessageId:{},Terminal:{}", copiedMessage.getId(), receiverTerminal);
            }
            if (!REACHED_MESSAGES.asMap().containsKey(cacheKey)) {
                throw new RetryException();
            }
        }
    }


    private String cacheKey(Long messageId, String userId, ImTerminalType terminalType) {
        return messageId + ":" + userId + ":" + terminalType.name();
    }

    @Override
    protected void doProcess(ChannelHandlerContext ctx, MessageReceivedNotice notice) {
        String userId = notice.getUserId();
        log.info("收到终端已接受消息通知。MessageId:{},Terminal:{}", notice.getMessageId(), userId);
        String cacheKey = cacheKey(notice.getMessageId(), userId, notice.getTerminalType());
        REACHED_MESSAGES.put(cacheKey, "");
    }

    @Override
    public ImInfoType supportType() {
        return ImInfoType.MESSAGE_RECEIVED;
    }
}
