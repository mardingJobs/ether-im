package cn.ether.im.push.processor.mq;

import cn.ether.im.common.model.message.ImChatMessage;


public interface ChatMessageProcess {


    /**
     * 处理非长连接收到的消息数据
     *
     * @param message
     */
    default void process(ImChatMessage message) {
    }


}
