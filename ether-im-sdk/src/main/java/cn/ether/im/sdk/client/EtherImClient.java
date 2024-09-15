package cn.ether.im.sdk.client;

import cn.ether.im.common.model.message.ImGroupMessage;
import cn.ether.im.common.model.message.ImPersonalMessage;

/**
 * * @Author: jack
 * * @Date    2024/9/15 15:16
 * * @Description
 **/
public interface EtherImClient {

    /**
     * 发送单聊消息
     *
     * @param personalMessage
     */
    String sendPersonalMessage(ImPersonalMessage personalMessage);

    /**
     * 发送群聊消息
     *
     * @param groupMessage
     */
    String sendGroupMessage(ImGroupMessage groupMessage);


}
