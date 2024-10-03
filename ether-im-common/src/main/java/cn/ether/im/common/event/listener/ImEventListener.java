package cn.ether.im.common.event.listener;

import cn.ether.im.common.event.event.ImEvent;
import cn.ether.im.common.event.event.ImEventType;

import java.util.List;

/**
 * * todo
 * * 1、指定事件类型监听，已读和已推送的事件单独处理。
 * * @Author: jack
 * * @Date    2024/9/15 11:51
 * * @Description
 **/
public interface ImEventListener {

    void onEvent(ImEvent event) throws Exception;

    List<ImEventType> listenEventType();

}
