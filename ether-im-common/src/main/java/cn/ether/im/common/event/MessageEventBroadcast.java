package cn.ether.im.common.event;

import cn.ether.im.common.model.info.message.event.ImMessageEvent;

/**
 * * @Author: jack
 * * @Date    2024/9/15 12:01
 * * @Description
 **/
public interface MessageEventBroadcast {

    void broadcast(ImMessageEvent messageEvent);

}
