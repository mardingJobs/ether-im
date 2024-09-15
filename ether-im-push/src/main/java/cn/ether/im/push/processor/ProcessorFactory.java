package cn.ether.im.push.processor;


import cn.ether.im.common.enums.ImMessageType;
import cn.ether.im.common.util.SpringContextHolder;
import cn.ether.im.push.processor.channel.ChannelMessageProcess;
import cn.ether.im.push.processor.channel.HeartbeatProcess;
import cn.ether.im.push.processor.channel.PersonalAckMessageProcess;
import cn.ether.im.push.processor.mq.PersonalMessageProcess;
import cn.ether.im.push.processor.mq.TopicMessageProcess;
import org.springframework.context.ApplicationContext;

public class ProcessorFactory {

    public static TopicMessageProcess getTopicProcessor(ImMessageType type) {
        ApplicationContext context = SpringContextHolder.getApplicationContext();
        switch (type) {
            case PERSONAL:
                return context.getBean(PersonalMessageProcess.class);
            default:
                return null;
        }
    }


    public static ChannelMessageProcess getChannelProcessor(ImMessageType type) {
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