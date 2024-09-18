package cn.ether.im.push.processor.chat;

import cn.ether.im.common.enums.ImMessageEventType;
import cn.ether.im.common.model.message.ImChatMessage;
import cn.ether.im.common.model.message.ImMessageEvent;
import cn.ether.im.common.model.user.ImUserTerminal;
import cn.ether.im.push.cache.UserChannelCache;
import cn.ether.im.push.mq.ImMessageEventProducer;
import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * * @Author: Martin
 * * @Date    2024/9/14 23:22
 * * @Description
 **/
@Slf4j
@Component
public class DefaultMessageProcess implements ChatMessageProcess {

    @Resource
    private ImMessageEventProducer eventProducer;


    @Override
    public void process(ImChatMessage message) {
        List<ImUserTerminal> receiverTerminals = message.getReceiverTerminals();
        for (ImUserTerminal terminal : receiverTerminals) {
            ChannelHandlerContext ctx = UserChannelCache.getChannelCtx(terminal.getUserId(),
                    terminal.getTerminalType().toString());
            if (ctx != null) {
                ImChatMessage copiedMessage = BeanUtil.copyProperties(message, ImChatMessage.class, "receiverTerminals", "receivers");
                copiedMessage.setReceivers(null);
                copiedMessage.setReceiverTerminals(null);
                // 写入并刷新消息，这里无法知道消息是否触达终端
                ctx.writeAndFlush(copiedMessage);
                // writeAndFlush 后，如果抛出异常，会导致重复消费。
                publishMessageEvent(message, terminal);
            }
        }
    }

    private void publishMessageEvent(ImChatMessage message, ImUserTerminal terminal) {
        try {
            // 发送消息已推送事件
            ImMessageEvent messageEvent = new ImMessageEvent();
            messageEvent.setMessageId(message.getId());
            messageEvent.setEventType(ImMessageEventType.PUSHED);
            messageEvent.setTerminal(terminal);
            messageEvent.setMessageType(message.getType());
            messageEvent.setEventTime(System.currentTimeMillis());
            eventProducer.publish(messageEvent);
        } catch (Exception e) {
            log.error("发布消息已推送事件失败,MessageEvent:{}", JSON.toJSONString(message), e);
        }
    }
}
