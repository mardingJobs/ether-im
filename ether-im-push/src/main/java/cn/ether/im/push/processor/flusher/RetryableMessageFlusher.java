package cn.ether.im.push.processor.flusher;

/**
 * * @Author: Martin(微信：martin-jobs)
 * * @Date    2024/9/24 00:21
 * * @Description
 * * @Github <a href="https://github.com/mardingJobs">Github链接</a>
 **/

import cn.ether.im.client.common.enums.ImInfoType;
import cn.ether.im.common.cache.RemoteCacheService;
import cn.ether.im.common.constants.ImConstants;
import cn.ether.im.common.exception.RetryException;
import cn.ether.im.common.model.message.ImMessage;
import cn.ether.im.common.model.message.ImMessageReceived;
import cn.ether.im.common.model.user.ImUserTerminal;
import cn.ether.im.push.cache.UserChannelCache;
import cn.ether.im.push.processor.ImInfoProcessor;
import cn.hutool.core.bean.BeanUtil;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class RetryableMessageFlusher extends ImInfoProcessor<ImMessageReceived> implements ImMessageFlusher {

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    @Resource
    private RemoteCacheService remoteCacheService;

    @Retryable(value = RetryException.class, maxAttempts = 3, backoff = @Backoff(delay = 2000, multiplier = 1.5))
    @Override
    public boolean flush(ImUserTerminal receiverTerminal, ImMessage message) {
        if (isReceived(message.getId(), receiverTerminal)) {
            log.info("消息已触达,MessageId:{},Terminal:{}", message.getId(), receiverTerminal);
            return true;
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
            throw new RetryException();
        }
        return false;
    }

    private boolean isReceived(Long messageId, ImUserTerminal terminal) {
        return !remoteCacheService.isMemberSet(ImConstants.UN_RECEIVED_MSG_PREFIX + messageId, terminal.toString());
    }


    @Override
    protected void doProcess(ChannelHandlerContext ctx, ImMessageReceived notice) {
        ImUserTerminal terminal = UserChannelCache.getUserTerminal(ctx);
        log.info("终端已接受消息。MessageId:{},Terminal:{}", notice.getMessageId(), terminal);
        remoteCacheService.removeSet(ImConstants.UN_RECEIVED_MSG_PREFIX + notice.getMessageId(), terminal.toString());
    }

    @Override
    public ImInfoType supportType() {
        return ImInfoType.MESSAGE_RECEIVED;
    }
}
