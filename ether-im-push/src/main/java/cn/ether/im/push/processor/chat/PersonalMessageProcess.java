package cn.ether.im.push.processor.chat;

import cn.ether.im.common.model.message.ImChatMessage;
import cn.ether.im.common.model.user.ImUserTerminal;
import cn.ether.im.push.cache.UserChannelCache;
import cn.hutool.core.bean.BeanUtil;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * * @Author: Martin
 * * @Date    2024/9/14 23:22
 * * @Description
 **/
@Slf4j
@Component
public class PersonalMessageProcess implements ChatMessageProcess {


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
                ctx.writeAndFlush(copiedMessage);
            }
        }
    }
}
