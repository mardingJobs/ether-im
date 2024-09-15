package cn.ether.im.message.service.impl;


import cn.ether.im.common.model.message.ImPersonalMessage;
import cn.ether.im.message.dto.PersonalChatMessageReq;
import cn.ether.im.message.service.MessageService;
import cn.ether.im.sdk.client.EtherImClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * * @Author: Martin
 * * @Date    2024/9/15 14:58
 * * @Description
 **/
@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private EtherImClient etherImClient;

    /**
     * @param req
     */
    @Override
    public void sendMessage(PersonalChatMessageReq req) {
        ImPersonalMessage personalMessage = new ImPersonalMessage();
        personalMessage.setSender(req.getSender());
        personalMessage.setReceivers(Collections.singletonList(req.getReceiver()));
        personalMessage.setContent(req.getContent());
        personalMessage.setContentType(req.getContentType());
        etherImClient.sendPersonalMessage(personalMessage);
    }
}
