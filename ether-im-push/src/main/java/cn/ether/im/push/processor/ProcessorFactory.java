package cn.ether.im.push.processor;


import cn.ether.im.common.enums.ImMessageType;
import cn.ether.im.common.util.SpringContextHolder;
import org.springframework.context.ApplicationContext;

public class ProcessorFactory {

    public static MessageProcessor getProcessor(ImMessageType type) {
        ApplicationContext context = SpringContextHolder.getApplicationContext();
        switch (type) {
            //登录
            case TOKEN:
                return context.getBean(TokenMessageProcessor.class);
            case HEART_BEAT:
                return context.getBean(HeartbeatProcessor.class);
            case PERSONAL:
                return context.getBean(PersonalMessageProcessor.class);
            default:
                return null;

        }
    }
}