package cn.ether.im.common.event.broadcast;

import cn.ether.im.common.event.event.ImEvent;

/**
 * * @Author: jack
 * * @Date    2024/9/15 12:01
 * * @Description
 **/
public interface ImEventBroadcast<T extends ImEvent> {

    void broadcast(T event);

}
