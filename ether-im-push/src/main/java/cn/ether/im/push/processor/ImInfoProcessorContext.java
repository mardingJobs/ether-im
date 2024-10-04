package cn.ether.im.push.processor;

import cn.ether.im.client.common.enums.ImInfoType;
import cn.ether.im.client.common.model.ImInfo;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * * @Author: Martin(微信：martin-jobs)
 * * @Date    2024/9/30 20:55
 * * @Description
 * * @Github <a href="https://github.com/mardingJobs">Github链接</a>
 **/
@Component
public class ImInfoProcessorContext {

    @Resource
    private Map<ImInfoType, ImInfoProcessor> processorMap;

    public <T> void process(ChannelHandlerContext ctx, ImInfo<T> info) {
        ImInfoProcessor<T> imInfoProcessor = processorMap.get(info.getType());
        T info1 = info.getInfo();
        imInfoProcessor.doProcess(ctx, (T) info1);
    }


}
