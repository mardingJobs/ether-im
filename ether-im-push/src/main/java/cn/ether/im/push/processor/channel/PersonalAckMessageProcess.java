package cn.ether.im.push.processor.channel;

import cn.ether.im.common.model.message.ImChatMessageAck;
import cn.ether.im.common.mq.ImMessageSender;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 单聊客户端确认收到消息
 * * @Author: Martin
 * * @Date    2024/9/14 23:53
 * * @Description
 **/
@Slf4j
@Component
public class PersonalAckMessageProcess implements SystemMessageProcess<ImChatMessageAck> {

    @Autowired
    private ImMessageSender messageSender;

    @Override
    public void process(ChannelHandlerContext ctx, ImChatMessageAck message) {
//        String id = message.getId();
//        log.info("终端已收到消息:{}", id);
//        ImMessageSendResult result = new ImMessageSendResult();
//        result.setMessage(message);
//        result.setSendCode(MessageSendCode.SUCCESS);
//        messageSender.send(result);
    }
}
