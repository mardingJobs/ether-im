package cn.ether.im.push.handler;

import cn.ether.im.common.cache.ImUserContextCache;
import cn.ether.im.common.model.user.ImUserTerminal;
import cn.ether.im.push.cache.UserChannelCache;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * * @Author: Martin(微信：martin-jobs)
 * * @Date    2024/10/3 15:55
 * * @Description
 * * @Github <a href="https://github.com/mardingJobs">Github链接</a>
 **/
@Component
public class ImUserHandler {

    @Resource
    ImUserContextCache contextCache;

    public void onUserLogin(ImUserTerminal userTerminal, ChannelHandlerContext ctx) {
        // 缓存当前用户终端和连接的push服务
        contextCache.bindPushServer(userTerminal);
        // 将当前用户终端和channel绑定
        UserChannelCache.bindChannel(userTerminal, ctx);
        // 发布用户登陆事件
    }

}
