/**
 * Copyright 2022-9999 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.ether.im.push.cache;

import cn.ether.im.common.constants.ImConstants;
import cn.ether.im.common.model.user.ImUserTerminal;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class UserChannelCache {

    /**
     * 缓存userId和ChannelHandlerContext的关系
     * 主要格式：Map<userId, Map<terminal, ctx>>
     */
    private static Map<String, Map<String, ChannelHandlerContext>> channelMap = new ConcurrentHashMap<>();

    public static void bindChannel(String userId, String channel, ChannelHandlerContext ctx) {
        channelMap.computeIfAbsent(userId, key -> new ConcurrentHashMap<>()).put(channel, ctx);
    }

    /**
     * 将用户终端和ChannelHandlerContext双向绑定
     *
     * @param userTerminal
     * @param ctx
     */
    public static void bindChannel(ImUserTerminal userTerminal, ChannelHandlerContext ctx) {
        channelMap.computeIfAbsent(userTerminal.getUserId(),
                key -> new ConcurrentHashMap<>()).put(userTerminal.getTerminalType().toString(), ctx);

        // 绑定用户到channel的属性
        AttributeKey<ImUserTerminal> userKey = AttributeKey.valueOf(ImConstants.USER_KEY);
        ctx.channel().attr(userKey).set(userTerminal);
    }

    /**
     * 获取用户终端
     *
     * @param ctx
     * @return
     */
    public static ImUserTerminal getUserTerminal(ChannelHandlerContext ctx) {
        AttributeKey<ImUserTerminal> userKey = AttributeKey.valueOf(ImConstants.USER_KEY);
        return ctx.channel().attr(userKey).get();
    }


    public static void removeChannelCtx(String userId, String terminal) {
        if (userId != null && terminal != null && channelMap.containsKey(userId)) {
            Map<String, ChannelHandlerContext> userChannelMap = channelMap.get(userId);
            if (userChannelMap.containsKey(terminal)) {
                userChannelMap.remove(terminal);
            }
        }
    }

    public static ChannelHandlerContext getChannelCtx(String userId, String terminal) {
        if (userId != null && terminal != null && channelMap.containsKey(userId)) {
            Map<String, ChannelHandlerContext> userChannelMap = channelMap.get(userId);
            if (userChannelMap.containsKey(terminal)) {
                return userChannelMap.get(terminal);
            }
        }
        return null;
    }

    public static Map<String, ChannelHandlerContext> getChannelCtx(String userId) {
        if (userId == null) {
            return null;
        }
        return channelMap.get(userId);
    }

}
