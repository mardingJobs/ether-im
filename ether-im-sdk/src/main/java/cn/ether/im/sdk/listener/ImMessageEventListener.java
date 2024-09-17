package cn.ether.im.sdk.listener;

import cn.ether.im.common.model.message.ImMessageEvent;

/**
 * * @Author: jack
 * * @Date    2024/9/15 11:51
 * * @Description
 **/
public interface ImMessageEventListener {

    void onMessageEvent(ImMessageEvent messageEvent);

}
