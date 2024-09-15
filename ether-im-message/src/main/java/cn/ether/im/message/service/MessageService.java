package cn.ether.im.message.service;

import cn.ether.im.common.model.message.ImPersonalMessage;

/**
 * * @Author: Martin
 * * @Date    2024/9/15 14:57
 * * @Description
 **/
public interface MessageService {

    public void sendMessage(ImPersonalMessage personalMessage);

}
