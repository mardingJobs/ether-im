package cn.ether.im.push.processor.chat.task;

import cn.ether.im.common.model.message.ImChatMessage;
import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * * @Author: Martin(微信：martin-jobs)
 * * @Date    2024/9/23 15:38
 * * @Description
 * * @Github <a href="https://github.com/mardingJobs">Github链接</a>
 **/
public class ImMessageRePushTask implements Delayed {

    @Getter
    private ImChatMessage message;

    private ChannelHandlerContext ctx;

    // 单位是毫秒
    private Long executeTime;

    /**
     * @param message
     * @param ctx
     * @param delayTime 单位是秒
     */
    public ImMessageRePushTask(ImChatMessage message, ChannelHandlerContext ctx, long delayTime) {
        this.message = message;
        this.ctx = ctx;
        this.executeTime = System.currentTimeMillis() + delayTime * 1000;
    }

    public long getDelay(TimeUnit unit) {
        return unit.convert(executeTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }

    public int compareTo(Delayed o) {
        return this.executeTime.compareTo(((ImMessageRePushTask) o).executeTime);
    }
}
