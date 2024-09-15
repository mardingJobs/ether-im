package cn.ether.im.common.helper;


import cn.ether.im.common.cache.DistributedCacheService;
import cn.ether.im.common.constants.ImConstants;
import cn.ether.im.common.enums.ImTerminalType;
import cn.ether.im.common.model.user.ImUser;
import cn.ether.im.common.model.user.ImUserTerminal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
     * 获取用户在线的终端类型
     *
     * @param userInfo
     * @return
     */
    public List<ImTerminalType> onlineTerminalTypes(ImUser userInfo) {
        String cacheKey = serverCacheKey(userInfo);
        Map<String, Object> terminalTypes = distributedCacheService.hashGet(cacheKey);
        if (terminalTypes == null || terminalTypes.isEmpty()) {
            return Collections.emptyList();
        }
        return terminalTypes.keySet().stream().map(ImTerminalType::valueOf).collect(Collectors.toList());
    }


    /**
     * 获取用户终端连接的PUSH服务
     *
     * @param userInfo
     * @return
     */
    public List<String> connectedServerIds(ImUser userInfo) {
        String cacheKey = serverCacheKey(userInfo);
        Map<Object, Object> map = distributedCacheService.hashGet(cacheKey);
        return map.values().stream().map(String::valueOf).collect(Collectors.toList());
    }

    /**
     * 将用户终端和服务绑定
     *
     * @param userTerminal
     * @param serverId
     */
    public void bindPushServer(ImUserTerminal userTerminal, Long serverId) {
        String cacheKey = serverCacheKey(userTerminal.getUser());
        distributedCacheService.hashPut(cacheKey, userTerminal.getTerminalType().toString(),
                serverId, ImConstants.ONLINE_TIMEOUT_SECONDS, TimeUnit.SECONDS);
    }

    /**
     * 获取缓存中的用户连接的PUSH服务的key
     * @param userInfo
     * @return1
     */
    public String serverCacheKey(ImUser userInfo) {
        String cacheKey = String.join(ImConstants.REDIS_KEY_SPLIT, userInfo.getGroup(),
                ImConstants.IM_USER_SERVER_ID, userInfo.getUserId());
        return cacheKey;
    }


}
