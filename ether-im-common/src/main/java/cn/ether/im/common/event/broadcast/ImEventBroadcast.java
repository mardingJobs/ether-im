package cn.ether.im.common.event.broadcast;

import cn.ether.im.common.model.info.message.event.ImEvent;

/**
 * * @Author: jack
 * * @Date    2024/9/15 12:01
 * * @Description
 **/
public interface ImEventBroadcast {

    void broadcast(ImEvent event);

}
