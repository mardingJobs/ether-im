package cn.ether.im.push.processor;


import cn.ether.im.common.enums.ChatMessageType;
import cn.ether.im.common.enums.ImMessageTypeEnum;
import cn.ether.im.common.enums.ImSystemMessageType;
import cn.ether.im.common.model.message.ImChatMessage;
import cn.ether.im.common.model.message.ImMessageWrapper;
import cn.ether.im.common.model.message.ImSystemAckMessage;
import cn.ether.im.common.model.message.ImSystemMessage;
import cn.ether.im.common.util.SpringContextHolder;
import cn.ether.im.push.processor.chat.ChatMessageProcess;
import cn.ether.im.push.processor.chat.DefaultMessageProcess;
import cn.ether.im.push.processor.system.HeartbeatProcess;
import cn.ether.im.push.processor.system.MessageEventProcess;
import cn.ether.im.push.processor.system.SystemMessageProcess;
import cn.ether.im.push.processor.system.TokenMessageProcess;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.context.ApplicationContext;

public class MessageProcessor {


    public static void processMessage(ImMessageWrapper message) {
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
        // ACK
        ImSystemAckMessage ackMessage = new ImSystemAckMessage(systemMessage.getSeq());
        ctx.writeAndFlush(ackMessage);

        ImSystemMessageType type = systemMessage.getType();
        SystemMessageProcess systemMessageProcess = systemMessageProcessor(type);
        systemMessageProcess.process(ctx, systemMessage);
    }


    public static ChatMessageProcess chatMessageProcessor(ChatMessageType type) {
        ApplicationContext context = SpringContextHolder.getApplicationContext();
        return context.getBean(DefaultMessageProcess.class);
//        switch (type) {
//            case PERSONAL:
//                return context.getBean(DefaultMessageProcess.class);
//            case GROUP:
//                return context.getBean(DefaultMessageProcess.class);
//            default:
//                return null;
//        }
    }


    public static SystemMessageProcess systemMessageProcessor(ImSystemMessageType type) {
        ApplicationContext context = SpringContextHolder.getApplicationContext();
        switch (type) {
            case TOKEN:
                return context.getBean(TokenMessageProcess.class);
            case HEART_BEAT:
                return context.getBean(HeartbeatProcess.class);
            case EVENT:
                return context.getBean(MessageEventProcess.class);
            default:
                return null;

        }
    }


}