package cn.ether.im.common.helper;


import cn.ether.im.common.cache.DistributedCacheService;
import cn.ether.im.common.constants.ImConstants;
import cn.ether.im.common.enums.ImTerminalType;
import cn.ether.im.common.model.user.ImUser;
import cn.ether.im.common.model.user.ImUserTerminal;
import cn.hutool.core.collection.CollectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 用户缓存相关的帮助类
 * * @Author: Martin
 * * @Date    2024/9/7 00:01
 * * @Description
 **/
@Component
public class ImUserCacheHelper {

    @Autowired
    private DistributedCacheService distributedCacheService;


    /**
     * 判断用户是否在线
     *
     * @param user
     * @return
     */
    public boolean online(ImUser user) {
        return CollectionUtil.isNotEmpty(getUserConnections(user));
    }


    /**
     * 返回用户在线时连接的push服务订阅的topic
     * 如果用户不在线，返回空列表
     *
     * @param user
     * @return
     */
    public List<String> relatedTopic(ImUser user) {
        List<String> serverIds = this.connectedServerIds(user);
        return serverIds.stream().map(serverId -> String.join(ImConstants.MQ_MESSAGE_KEY_SPLIT, user.getGroup(),
                ImConstants.IM_MESSAGE_PUSH_TOPIC, serverId)).collect(Collectors.toList());
    }


    /**
     * 获取用户在线的终端类型
     *
     * @param userInfo
     * @return
     */
    public List<ImTerminalType> onlineTerminalTypes(ImUser userInfo) {
        Map<String, Object> userConnections = getUserConnections(userInfo);
        if (userConnections == null || userConnections.isEmpty()) {
            return Collections.emptyList();
        }
        return userConnections.keySet().stream().map(ImTerminalType::valueOf).collect(Collectors.toList());
    }

    /**
     * 获取用户在线的终端信息
     *
     * @param userInfo
     * @return
     */
    public List<ImUserTerminal> onlineTerminals(ImUser userInfo) {
        Map<String, Object> userConnections = getUserConnections(userInfo);
        return userConnections.entrySet().stream().map(entry -> new ImUserTerminal(userInfo, ImTerminalType.valueOf(entry.getKey())))
                .collect(Collectors.toList());
    }


    /**
     * 获取用户终端连接的PUSH服务ID
     *
     * @param userInfo
     * @return
     */
    public List<String> connectedServerIds(ImUser userInfo) {
        Map<String, Object> userConnections = getUserConnections(userInfo);
        return userConnections.values().stream().map(String::valueOf).collect(Collectors.toList());
    }

    /**
     * 将用户终端和服务绑定
     *
     * @param userTerminal
     * @param serverId
     */
    public void bindPushServer(ImUserTerminal userTerminal, Long serverId) {
        String cacheKey = serverCacheKey(userTerminal);
        distributedCacheService.hashPut(cacheKey, userTerminal.getTerminalType().toString(),
                serverId, ImConstants.ONLINE_TIMEOUT_SECONDS, TimeUnit.SECONDS);
    }

    /**
     * 获取用户连接信息,key是用户终端 value是服务器ID
     *
     * @param userInfo
     * @return
     */
    public Map<String, Object> getUserConnections(ImUser userInfo) {
        Map<String, Object> map = distributedCacheService.hashGet(serverCacheKey(userInfo));
        return map;
    }

    /**
     * 获取缓存中的用户连接的PUSH服务的key
     *
     * @param userInfo
     * @return1
     */
    public String serverCacheKey(ImUser userInfo) {
        String cacheKey = String.join(ImConstants.REDIS_KEY_SPLIT, userInfo.getGroup(),
                ImConstants.IM_USER_SERVER_ID, userInfo.getUserId());
        return cacheKey;
    }


}
