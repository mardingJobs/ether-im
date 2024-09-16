package cn.ether.im.sdk.sender;

import cn.ether.im.common.model.message.ImGroupMessage;
import cn.ether.im.common.model.message.ImPersonalMessage;

/**
 * * @Author: Martin
 * * @Date    2024/9/15 15:22
 * * @Description
 **/
public interface ChatMessageSender {

    void sendPersonalMessage(ImPersonalMessage personalMessage);

    void sendGroupMessage(ImGroupMessage groupMessage);

}
