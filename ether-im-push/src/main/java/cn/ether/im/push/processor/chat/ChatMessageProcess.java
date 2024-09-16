package cn.ether.im.push.processor.chat;

import cn.ether.im.common.model.message.ImChatMessage;


public interface ChatMessageProcess {


    default void process(ImChatMessage message) {
    }


}
