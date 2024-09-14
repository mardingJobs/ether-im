package cn.ether.im.push.processor.mq;

import cn.ether.im.common.model.message.ImMessage;
import io.netty.channel.ChannelHandlerContext;


public interface TopicMessageProcess {

    /**
     * 处理长连接收到的消息数据
     */
    default void process(ChannelHandlerContext ctx, ImMessage message) {}

    /**
     * 处理非长连接收到的消息数据
     *
     * @param message
     */
    default void process(ImMessage message) {}


}
