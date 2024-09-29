package cn.ether.im.push.processor;


import cn.ether.im.common.enums.ImSysMessageType;
import cn.ether.im.common.model.info.message.ImMessage;
import cn.ether.im.common.model.info.sys.ImSysMessage;
import cn.ether.im.common.util.SpringContextHolder;
import cn.ether.im.push.processor.message.DefaultMessageProcess;
import cn.ether.im.push.processor.message.ImMessageProcess;
import cn.ether.im.push.processor.sys.HeartbeatProcess;
import cn.ether.im.push.processor.sys.SystemMessageProcess;
import cn.ether.im.push.processor.sys.TokenMessageProcess;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.context.ApplicationContext;

public class InfoProcessor {


    public static void processChatMessage(ImMessage chatMessage) {
        ApplicationContext context = SpringContextHolder.getApplicationContext();
        ImMessageProcess messageProcess = context.getBean(DefaultMessageProcess.class);
        messageProcess.process(chatMessage);
    }

    public static void processSystemMessage(ChannelHandlerContext ctx, ImSysMessage systemMessage) {
        ImSysMessageType type = systemMessage.getSystemMessageType();
        SystemMessageProcess systemMessageProcess = systemMessageProcessor(type);
        systemMessageProcess.process(ctx, systemMessage);
    }


    public static SystemMessageProcess systemMessageProcessor(ImSysMessageType type) {
        ApplicationContext context = SpringContextHolder.getApplicationContext();
        switch (type) {
            case TOKEN:
                return context.getBean(TokenMessageProcess.class);
            case HB:
                return context.getBean(HeartbeatProcess.class);
            default:
                return null;

        }
    }


}