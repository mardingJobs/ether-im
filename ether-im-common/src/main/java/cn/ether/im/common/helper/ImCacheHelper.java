package cn.ether.im.common.helper;


import cn.ether.im.common.cache.DistributedCacheService;
import cn.ether.im.common.constants.ImConstants;
import cn.ether.im.common.model.ImUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * * @Author: Martin
 * * @Date    2024/9/7 00:01
 * * @Description
 **/
@Component
public class ImCacheHelper {

    @Autowired
    private DistributedCacheService distributedCacheService;

    /**
     * 获取用户终端连接的是哪一个服务
     *
     * @param userInfo
     * @return
     */
    public String getServerId(ImUser userInfo) {
        String cacheKey = serverCacheKey(userInfo);
        String serverId = distributedCacheService.get(cacheKey);
        return serverId;
    }

    public void bindPushServer(ImUser user, Long serverId) {
        String cacheKey = serverCacheKey(user);
        distributedCacheService.set(cacheKey, serverId, ImConstants.ONLINE_TIMEOUT_SECONDS, TimeUnit.SECONDS);
    }


    public String serverCacheKey(ImUser userInfo) {
        String cacheKey = String.join(ImConstants.REDIS_KEY_SPLIT, userInfo.getGroup(),
                ImConstants.IM_USER_SERVER_ID, userInfo.getUserId(), userInfo.getTerminalType().toString());
        return cacheKey;
    }


}
