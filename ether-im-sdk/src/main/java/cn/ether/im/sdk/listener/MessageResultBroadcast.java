package cn.ether.im.sdk.listener;

import cn.ether.im.common.model.message.ImMessageSendResult;

/**
 * * @Author: jack
 * * @Date    2024/9/15 12:01
 * * @Description
 **/
public interface MessageResultBroadcast {

    void broadcast(ImMessageSendResult result);

}
