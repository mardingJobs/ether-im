package cn.ether.im.sdk.listener;

import cn.ether.im.common.model.message.ImMessageSendResult;

/**
 * * @Author: jack
 * * @Date    2024/9/15 11:51
 * * @Description
 **/
public interface MessageResultListener {


    void onMessageResult(ImMessageSendResult result);

}
