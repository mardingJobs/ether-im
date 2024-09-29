package cn.ether.im.common.event.listener;

import cn.ether.im.common.model.info.message.event.ImMessageEvent;

/**
 * * todo
 * * 1、指定事件类型监听，已读和已推送的事件单独处理。
 * * @Author: jack
 * * @Date    2024/9/15 11:51
 * * @Description
 **/
public interface ImMessageEventListener {

    void onMessageEvent(ImMessageEvent messageEvent) throws Exception;

}
