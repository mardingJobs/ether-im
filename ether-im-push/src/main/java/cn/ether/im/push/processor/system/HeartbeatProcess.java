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
package cn.ether.im.push.processor.system;

import cn.ether.im.common.cache.RemoteCacheService;
import cn.ether.im.common.constants.ImConstants;
import cn.ether.im.common.helper.ImUserContextHelper;
import cn.ether.im.common.model.message.ImHeartbeatMessage;
import cn.ether.im.common.model.user.ImUserTerminal;
import cn.ether.im.push.cache.UserChannelCache;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class HeartbeatProcess implements SystemMessageProcess<ImHeartbeatMessage> {

    @Autowired
    private RemoteCacheService remoteCacheService;

    @Autowired
    private ImUserContextHelper cacheHelper;

    @Override
    public void process(ChannelHandlerContext ctx, ImHeartbeatMessage message) {
        ImUserTerminal userTerminal = UserChannelCache.getUserTerminal(ctx);
        if (userTerminal == null) {
            log.warn("UserTerminal is null,close channel.RemoteAddress:{}", ctx.channel().remoteAddress());
            ctx.close();
            return;
        }
        // 延续在线时间
        String cacheKey = cacheHelper.serverCacheKey(userTerminal);
        remoteCacheService.expire(cacheKey, ImConstants.ONLINE_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        // 心跳次数清零,重新计算心跳超时时间
        ctx.channel().attr(AttributeKey.valueOf(ImConstants.HEARTBEAT_TIMES)).set(0);
    }
}
