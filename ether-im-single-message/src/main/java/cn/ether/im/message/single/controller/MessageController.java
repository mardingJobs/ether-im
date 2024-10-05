package cn.ether.im.message.single.controller;

import cn.ether.im.message.single.handler.ImMessageHandler;
import cn.ether.im.message.single.model.dto.MessageSendReq;
import cn.ether.im.message.single.model.vo.Resp;
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
@RequestMapping("/message")
@RestController
public class MessageController {

    @Resource
    private ImMessageHandler messageHandler;


    /**
     * 发送单聊消息
     * 主要用于提升发送消息性能，不用数据库
     *
     * @param req
     * @return
     */
    @Operation(summary = "发送消息", description = "如果发送成功的话，会返回消息ID")
    @PostMapping("/send")
    public Resp send(@RequestBody MessageSendReq req) throws Exception {
        String messageId = messageHandler.sendSingleMessage(req);
        return Resp.success(messageId);
    }


    /**
     * 异步发送单聊消息
     *
     * @param req
     * @return
     */
    @Operation(summary = "异步发送消息")
    @PostMapping("/send/async")
    public Resp sendAsync(@RequestBody MessageSendReq req) throws Exception {
        messageHandler.asyncSendMessage(req);
        return Resp.success();
    }


}
