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
package cn.ether.im.push.processor.channel;

import cn.ether.im.common.cache.DistributedCacheService;
import cn.ether.im.common.constants.ImConstants;
import cn.ether.im.common.helper.ImCacheHelper;
import cn.ether.im.common.model.message.ImHeartbeatMessage;
import cn.ether.im.common.model.user.ImUserTerminal;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class HeartbeatProcess implements SystemMessageProcess<ImHeartbeatMessage> {

    @Autowired
    private DistributedCacheService distributedCacheService;

    @Autowired
    private ImCacheHelper cacheHelper;

    @Value("${heartbeat.count:10}")
    private Integer heartbeatCount;

    @Override
    public void process(ChannelHandlerContext ctx, ImHeartbeatMessage message) {
        //响应ws
        this.response(ctx);
        //设置属性
        AttributeKey<Long> heartBeatAttr = AttributeKey.valueOf(ImConstants.HEARTBEAT_TIMES);
        Long heartbeatTimes = ctx.channel().attr(heartBeatAttr).get();
        if (heartbeatTimes == null) heartbeatTimes = 0L;
        ctx.channel().attr(heartBeatAttr).set(++heartbeatTimes);
        if (heartbeatTimes % heartbeatCount == 0) {
            //心跳10次，用户在线状态续命一次
            AttributeKey<ImUserTerminal> userAttr = AttributeKey.valueOf(ImConstants.USER_KEY);
            ImUserTerminal userTerminal = ctx.attr(userAttr).get();
            if (userTerminal == null) return;
            String cacheKey = cacheHelper.serverCacheKey(userTerminal);
            distributedCacheService.expire(cacheKey, ImConstants.ONLINE_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        }
    }


    /**
     * 响应ws的数据
     */
    private void response(ChannelHandlerContext ctx) {
        ImHeartbeatMessage message = new ImHeartbeatMessage();
        ctx.channel().writeAndFlush(message);
    }
}
