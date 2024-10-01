package cn.ether.im.message.controller;

import cn.ether.im.message.model.dto.ChatMessagePullReq;
import cn.ether.im.message.model.dto.ChatMessagePullResult;
import cn.ether.im.message.model.dto.GroupMessageSendReq;
import cn.ether.im.message.model.dto.PersonalMessageSendReq;
import cn.ether.im.message.model.vo.Resp;
import cn.ether.im.message.service.ChatMessageService;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(
        name = "MessageController",
        description = "对话消息接口",
        externalDocs = @ExternalDocumentation(
                description = "对话消息接口文档",
                url = "https://www.google.com"))
@RequestMapping("/chatMessage")
@RestController
public class ChatMessageController {

    @Resource
    private ChatMessageService messageService;


    /**
     * 发送单聊消息
     * 主要用于提升发送消息性能，不用数据库
     *
     * @param req
     * @return
     */
    @Operation(summary = "发送单聊消息", description = "如果发送成功的话，会返回消息ID")
    @PostMapping("/send/personal")
    public Resp sendPersonal(@RequestBody PersonalMessageSendReq req) throws Exception {
        String messageId = messageService.sendPersonalMessage(req);
        return Resp.success(messageId);
    }


    /**
     * 异步发送单聊消息
     *
     * @param req
     * @return
     */
    @Operation(summary = "异步发送单聊消息")
    @PostMapping("/send/personal/async")
    public Resp sendPersonalAsync(@RequestBody PersonalMessageSendReq req) throws Exception {
        messageService.asyncSendPersonalMessage(req);
        return Resp.success();
    }

    @Operation(summary = "发送群聊消息", description = "如果发送成功的话，会返回消息ID")
    @PostMapping("/send/group")
    public Resp sendGroup(@RequestBody GroupMessageSendReq req) throws Exception {
        String messageId = messageService.sendGroupMessage(req);
        return Resp.success(messageId);
    }

    /**
     * 拉取最近用户的收件箱消息和发件箱消息
     *
     * @param req
     * @return
     */
    @Operation(summary = "拉取最近一个月内的消息", description = "拉取最近的消息，包括收件箱和发件箱消息各100条")
    @PostMapping("/pullRecentMessages")
    public Resp pullRecentMessages(@RequestBody ChatMessagePullReq req) {
        ChatMessagePullResult result = messageService.pullRecentMessages(req);
        return Resp.success(result);
    }


}
