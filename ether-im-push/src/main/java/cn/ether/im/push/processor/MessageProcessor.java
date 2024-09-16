package cn.ether.im.push.processor;


import cn.ether.im.common.enums.ChatMessageType;
import cn.ether.im.common.enums.ImMessageTypeEnum;
import cn.ether.im.common.enums.SystemMessageType;
import cn.ether.im.common.model.message.ImChatMessage;
import cn.ether.im.common.model.message.ImMessage;
import cn.ether.im.common.model.message.ImSystemMessage;
import cn.ether.im.common.util.SpringContextHolder;
import cn.ether.im.push.processor.chat.ChatMessageProcess;
import cn.ether.im.push.processor.chat.PersonalMessageProcess;
import cn.ether.im.push.processor.system.HeartbeatProcess;
import cn.ether.im.push.processor.system.PersonalAckMessageProcess;
import cn.ether.im.push.processor.system.SystemMessageProcess;
import cn.ether.im.push.processor.system.TokenMessageProcess;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.context.ApplicationContext;

public class MessageProcessor {


    public static void processMessage(ImMessage message) {
        ImMessageTypeEnum messageType = message.getType();
        switch (messageType) {
            case CHAT:
                processChatMessage((ImChatMessage) message.getMessage());
                break;
            case SYSTEM:
                processSystemMessage(null, (ImSystemMessage) message.getMessage());
                break;
            default:
                break;
        }


    }


    public static void processChatMessage(ImChatMessage chatMessage) {
        ChatMessageType type = chatMessage.getType();
        ChatMessageProcess chatMessageProcess = chatMessageProcessor(type);
        chatMessageProcess.process(chatMessage);
    }

    public static void processSystemMessage(ChannelHandlerContext ctx, ImSystemMessage systemMessage) {
        SystemMessageType type = systemMessage.getType();
        SystemMessageProcess systemMessageProcess = systemMessageProcessor(type);
        systemMessageProcess.process(ctx, systemMessage);
    }


    public static ChatMessageProcess chatMessageProcessor(ChatMessageType type) {
        ApplicationContext context = SpringContextHolder.getApplicationContext();
        switch (type) {
            case PERSONAL:
                return context.getBean(PersonalMessageProcess.class);
            default:
                return null;
        }
    }


    public static SystemMessageProcess systemMessageProcessor(SystemMessageType type) {
        ApplicationContext context = SpringContextHolder.getApplicationContext();
        switch (type) {
            case TOKEN:
                return context.getBean(TokenMessageProcess.class);
            case HEART_BEAT:
                return context.getBean(HeartbeatProcess.class);
            case ACK_RECEIVE:
                return context.getBean(PersonalAckMessageProcess.class);
            default:
                return null;

        }
    }


}