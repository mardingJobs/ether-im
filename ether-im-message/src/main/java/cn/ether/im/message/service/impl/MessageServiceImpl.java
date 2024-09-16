package cn.ether.im.message.service.impl;


import cn.ether.im.common.model.message.ImGroupMessage;
import cn.ether.im.common.model.message.ImPersonalMessage;
import cn.ether.im.message.dto.GroupChatMessageReq;
import cn.ether.im.message.dto.PersonalChatMessageReq;
import cn.ether.im.message.service.MessageService;
import cn.ether.im.sdk.client.EtherImClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public String sendPersonalMessage(PersonalChatMessageReq req) {
        ImPersonalMessage personalMessage = new ImPersonalMessage();
        personalMessage.setSender(req.getSender());
        personalMessage.getReceivers().add(req.getReceiver());
        personalMessage.setContent(req.getContent());
        personalMessage.setContentType(req.getContentType());
        return etherImClient.sendChatMessage(personalMessage);
    }

    /**
     * @param req
     */
    @Override
    public String sendGroupMessage(GroupChatMessageReq req) {
        ImGroupMessage imGroupMessage = new ImGroupMessage();
        imGroupMessage.setSender(req.getSender());
        imGroupMessage.setReceivers(req.getReceivers());
        imGroupMessage.setContent(req.getContent());
        imGroupMessage.setContentType(req.getContentType());
        return etherImClient.sendChatMessage(imGroupMessage);
    }
}
