package cn.ether.im.message.group.controller;

import cn.ether.im.common.util.SnowflakeUtil;
import cn.ether.im.message.group.model.vo.Resp;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * * @Author: Martin(微信：martin-jobs)
 * * @Date    2024/9/26 03:18
 * * @Description
 * * @Github <a href="https://github.com/mardingJobs">Github链接</a>
 **/
@Tag(
        name = "MessageController",
        description = "对话消息接口",
        externalDocs = @ExternalDocumentation(
                description = "对话消息接口文档",
                url = "https://www.google.com"))
@RequestMapping("/conversation")
@RestController
public class ConversationController {


    @Resource
    private SnowflakeUtil snowflakeUtil;

    @PostMapping("/create")
    public Resp createConversation() {
//        ImUserTerminal userTerminal = SessionContext.loggedUser();
//        ImConversationEntity conversationEntity = new ImConversationEntity();
//        conversationEntity.setCreatorId(userTerminal.getUserId());
//        conversationEntity.setId(snowflakeUtil.nextId());
//        conversationEntity.setCreateTime(new Date());
//        conversationEntity.setUpdateTime(new Date());
//        boolean saved = conversationService.save(conversationEntity);
//        if (saved) {
//            return Resp.success(conversationEntity);
//        }
        return Resp.fail("操作失败");
    }

}
