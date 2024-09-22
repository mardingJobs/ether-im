package cn.ether.im.message.controller;

import cn.ether.im.common.model.ImChatMessageSentResult;
import cn.ether.im.message.model.dto.ChatMessageQueryReq;
import cn.ether.im.message.model.dto.ChatMessageSendReq;
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

    /**
     * 发送消息
     *
     * @param req
     * @return
     */
    @PostMapping("/send/")
    public Resp sendPersonalMessage(@RequestBody ChatMessageSendReq req) {
        ImChatMessageSentResult result = messageService.sendMessage(req);
        return Resp.success(result);
    }

    /**
     * 查询消息
     *
     * @param req
     * @return
     */
    @PostMapping("/query")
    public Resp queryMessage(@RequestBody ChatMessageQueryReq req) {
        return Resp.success(null);
    }


}
