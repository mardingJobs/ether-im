package cn.ether.im.push.processor.mq;

import cn.ether.im.common.model.message.ImChatMessage;


public interface ChatMessageProcess {


    default void process(ImChatMessage message) {
    }


}
