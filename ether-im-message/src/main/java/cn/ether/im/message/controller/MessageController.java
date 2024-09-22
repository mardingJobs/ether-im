package cn.ether.im.message.controller;

import cn.ether.im.common.model.ImChatMessageSentResult;
import cn.ether.im.message.model.dto.ChatMessagePullReq;
import cn.ether.im.message.model.dto.ChatMessagePullResult;
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
    public Resp send(@RequestBody ChatMessageSendReq req) {
        ImChatMessageSentResult result = messageService.sendMessage(req);
        return Resp.success(result);
    }

    /**
     * 拉取最近用户的收件箱消息和发件箱消息
     *
     * @param req
     * @return
     */
    @PostMapping("/pullRecentMessages")
    public Resp pullRecentMessages(@RequestBody ChatMessagePullReq req) {
        ChatMessagePullResult result = messageService.pullRecentMessages(req);
        return Resp.success(result);
    }


}
