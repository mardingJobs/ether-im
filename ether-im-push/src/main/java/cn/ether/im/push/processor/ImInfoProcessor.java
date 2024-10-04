package cn.ether.im.push.processor;

import cn.ether.im.client.common.enums.ImInfoType;
import io.netty.channel.ChannelHandlerContext;

/**
 * * @Author: Martin(微信：martin-jobs)
 * * @Date    2024/9/30 20:42
 * * @Description
 * * @Github <a href="https://github.com/mardingJobs">Github链接</a>
 **/
public abstract class ImInfoProcessor<T> {

    protected abstract void doProcess(ChannelHandlerContext ctx, T info);

    public abstract ImInfoType supportType();

}
