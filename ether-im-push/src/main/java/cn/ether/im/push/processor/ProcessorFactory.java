package cn.ether.im.push.processor;


import cn.ether.im.common.enums.ImMessageType;
import cn.ether.im.common.util.SpringContextHolder;

public class ProcessorFactory {

    public static MessageProcessor getProcessor(ImMessageType type){
        switch (type){
            //登录
            case TOKEN:
                return SpringContextHolder.getApplicationContext().getBean(TokenMessageProcessor.class);
            default:
                return null;

        }
    }
}