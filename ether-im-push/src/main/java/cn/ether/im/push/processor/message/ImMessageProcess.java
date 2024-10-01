package cn.ether.im.push.processor.message;

import cn.ether.im.common.enums.ImInfoType;
import cn.ether.im.common.exception.RetryException;
import cn.ether.im.common.model.info.message.ImMessage;
import cn.ether.im.common.model.user.ImUserTerminal;
import cn.ether.im.common.util.ThreadPoolUtils;
import cn.ether.im.push.processor.ImInfoProcessor;
import cn.ether.im.push.processor.flusher.ImMessageFlusher;
import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * * @Author: Martin
 * * @Date    2024/9/14 23:22
 * * @Description
 **/
@Slf4j
@Component
public class ImMessageProcess extends ImInfoProcessor<ImMessage> {

    @Resource
    private ImMessageFlusher messageFlusher;

    private ThreadPoolExecutor flushExecutor = ThreadPoolUtils.createExecutor(10, 1024);


    @Override
    public void doProcess(ChannelHandlerContext ctx, ImMessage message) {
        List<ImUserTerminal> receiverTerminals = message.getReceiverTerminals();

        for (ImUserTerminal terminal : receiverTerminals) {
            try {
                flushExecutor.execute(() -> {
                    messageFlusher.flush(terminal, message);
                });
            } catch (RetryException e) {
                log.warn("重复推送消息后未收到触达消息，MessageId:{},UserTerminal:{}", message.getId(), JSON.toJSONString(terminal));
            } catch (Exception e) {
                log.error("消息推送失败,MessageId:{}", message.getId(), e);
            }
        }
    }

    @Override
    public ImInfoType supportType() {
        return ImInfoType.MESSAGE;
    }
}
