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
package cn.ether.im.push.processor;

import cn.ether.im.common.cache.ImUserContextCache;
import cn.ether.im.common.cache.RemoteCacheService;
import cn.ether.im.common.constants.ImConstants;
import cn.ether.im.common.enums.ImInfoType;
import cn.ether.im.common.model.info.sys.ImHeartbeatInfo;
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
public class HeartbeatProcess extends ImInfoProcessor<ImHeartbeatInfo> {

    @Autowired
    private RemoteCacheService remoteCacheService;

    @Autowired
    private ImUserContextCache cacheHelper;

    @Override
    public void doProcess(ChannelHandlerContext ctx, ImHeartbeatInfo message) {
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

    @Override
    public ImInfoType supportType() {
        return ImInfoType.HEART_BEAT;
    }
}
