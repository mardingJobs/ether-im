package cn.ether.im.push.processor;

import cn.ether.im.common.constants.ImConstants;
import cn.ether.im.common.helper.ImCacheHelper;
import cn.ether.im.common.model.ImUser;
import cn.ether.im.common.model.message.ImMessage;
import cn.ether.im.common.model.message.ImTokenMessage;
import cn.ether.im.common.util.JwtUtils;
import cn.ether.im.push.cache.UserChannelCache;
import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
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
public class TokenMessageProcessor implements MessageProcessor {


    @Value("${jwt.accessToken.secret}")
    private String accessTokenSecret;

    @Resource
    private ImCacheHelper cacheHelper;


    @Value("${server.id}")
    private Long serverId;


    /**
     * 处理长连接收到的消息数据
     *
     * @param ctx
     * @param tokenMessage
     */
    @Override
    public void process(ChannelHandlerContext ctx, ImMessage tokenMessage) {
        String token = tokenMessage.getContent();
        Boolean signed = JwtUtils.checkSign(token, accessTokenSecret);
        if (!signed) {
            log.info("Token is invalid，token：{}", token);
            ctx.channel().close();
            return;
        }

        ImUser imUser = JSON.parseObject(JwtUtils.getInfo(token), ImUser.class);
        if (imUser == null) {
            log.warn("imUser is null");
            return;
        }
        // 缓存当前用户终端和连接的push服务
        cacheHelper.bindPushServer(imUser, serverId);
        // 将当前用户终端和channel绑定
        UserChannelCache.bindChannel(imUser, ctx);

        // 绑定用户到channel的属性
        AttributeKey<ImUser> userKey = AttributeKey.valueOf(ImConstants.USER_KEY);
        ctx.channel().attr(userKey).set(imUser);

        ImTokenMessage tokenResp = new ImTokenMessage();
        ctx.channel().writeAndFlush(tokenResp);
    }

    /**
     * 处理非长连接收到的消息数据
     *
     * @param message
     */
    @Override
    public void process(ImMessage message) {

    }
}
