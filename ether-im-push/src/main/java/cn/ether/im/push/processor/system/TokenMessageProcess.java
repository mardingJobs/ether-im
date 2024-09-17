package cn.ether.im.push.processor.system;

import cn.ether.im.common.helper.ImUserCacheHelper;
import cn.ether.im.common.model.message.ImTokenMessage;
import cn.ether.im.common.model.user.ImUserTerminal;
import cn.ether.im.common.util.JwtUtils;
import cn.ether.im.push.cache.UserChannelCache;
import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * * @Author: Martin
 * * @Date    2024/9/3 16:17
 * * @Description
 **/
@Slf4j
@Component
public class TokenMessageProcess implements SystemMessageProcess<ImTokenMessage> {


    @Value("${jwt.accessToken.secret}")
    private String accessTokenSecret;

    @Resource
    private ImUserCacheHelper cacheHelper;


    @Value("${server.id}")
    private Long serverId;


    /**
     * 处理长连接收到的消息数据
     *
     * @param ctx
     * @param tokenMessage
     */
    @Override
    public void process(ChannelHandlerContext ctx, ImTokenMessage tokenMessage) {
        String token = tokenMessage.getToken();
        Boolean signed = JwtUtils.checkSign(token, accessTokenSecret);
        if (!signed) {
            log.info("Token is invalid，token：{}", token);
            ctx.channel().close();
            return;
        }

        ImUserTerminal imUserTerminal = JSON.parseObject(JwtUtils.getInfo(token), ImUserTerminal.class);
        if (imUserTerminal == null) {
            log.warn("imUserTerminal is null");
            ctx.channel().close();
            return;
        }
        // 缓存当前用户终端和连接的push服务
        cacheHelper.bindPushServer(imUserTerminal, serverId);
        // 将当前用户终端和channel绑定
        UserChannelCache.bindChannel(imUserTerminal, ctx);
    }

}
