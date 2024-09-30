package cn.ether.im.push.util;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

/**
 * * @Author: Martin(微信：martin-jobs)
 * * @Date    2024/9/30 13:31
 * * @Description
 * * @Github <a href="https://github.com/mardingJobs">Github链接</a>
 **/
public class ChannelHandlerContextUtil {

    public static void setAttr(ChannelHandlerContext ctx, String key, Object value) {
        AttributeKey<Object> userKey = AttributeKey.valueOf(key);
        ctx.channel().attr(userKey).set(value);
    }

    public static Object getAttr(ChannelHandlerContext ctx, String key) {
        AttributeKey<Object> userKey = AttributeKey.valueOf(key);
        return ctx.channel().attr(userKey).get();
    }

}
