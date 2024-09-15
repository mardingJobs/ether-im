package cn.ether.im.message.controller;

import cn.ether.im.message.domain.Resp;
import cn.ether.im.message.dto.PersonalChatMessageReq;
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
    @PostMapping("/sendMessage/personal/")
    public Resp sendPersonalMessage(@RequestBody PersonalChatMessageReq req) {
        messageService.sendMessage(req);
        return Resp.success();
    }


    // 发送群聊消息

}