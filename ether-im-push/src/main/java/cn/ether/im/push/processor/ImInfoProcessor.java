package cn.ether.im.push.processor;

import cn.ether.im.common.enums.ImInfoType;
import cn.ether.im.common.model.info.ImInfo;
import io.netty.channel.ChannelHandlerContext;

/**
 * * @Author: Martin(微信：martin-jobs)
 * * @Date    2024/9/30 20:42
 * * @Description
 * * @Github <a href="https://github.com/mardingJobs">Github链接</a>
 **/
public abstract class ImInfoProcessor<T extends ImInfo> {

    public void process(ChannelHandlerContext ctx, ImInfo info) {
        T downcast = downcast(info);
        doProcess(ctx, downcast);
    }

    protected abstract void doProcess(ChannelHandlerContext ctx, T info);

    public abstract ImInfoType supportType();

    public T downcast(ImInfo info) {
        return (T) info;
    }

}
