package cn.ether.im.message.controller;

import cn.ether.im.common.model.ImChatMessageSentResult;
import cn.ether.im.message.model.dto.GroupChatMessageReq;
import cn.ether.im.message.model.dto.PersonalChatMessageReq;
import cn.ether.im.message.model.dto.Resp;
import cn.ether.im.message.service.MessageService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * * @Author: Martin
 * * @Date    2024/9/15 12:29
 * * @Description
 **/
@RequestMapping("/message")
@RestController
public class MessageController {

    @Resource
    private MessageService messageService;

    // 发送单聊消息
    @PostMapping("/personal/send/")
    public Resp sendPersonalMessage(@RequestBody PersonalChatMessageReq req) {
        ImChatMessageSentResult result = messageService.sendPersonalMessage(req);
        return Resp.success(result);
    }

//    @PostMapping("/personal//")
//    public Resp sendPersonalMessage(@RequestBody PersonalChatMessageReq req) {
//        ImChatMessageSentResult result = messageService.sendPersonalMessage(req);
//        return Resp.success(result);
//    }


    // 发送群聊消息
    @PostMapping("/group/send/")
    public Resp sendGroupMessage(@RequestBody GroupChatMessageReq req) {
        ImChatMessageSentResult result = messageService.sendGroupMessage(req);
        return Resp.success(result);
    }

}
