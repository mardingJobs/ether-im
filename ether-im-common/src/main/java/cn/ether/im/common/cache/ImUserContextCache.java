package cn.ether.im.common.cache;


import cn.ether.im.common.constants.ImConstants;
import cn.ether.im.common.enums.ImTerminalType;
import cn.ether.im.common.model.user.ImUserTerminal;
import cn.ether.im.common.util.SpringContextHolder;
import cn.hutool.core.collection.CollectionUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 用户缓存相关的帮助类
 * * @Author: Martin
 * * @Date    2024/9/7 00:01
 * * @Description
 **/
@Component
public class ImUserContextCache {

    @Autowired
    private RemoteCacheService remoteCacheService;


    /**
     * 返回在线的用户ID
     *
     * @param userIds
     * @return
     */
    public List<String> onlineUserIds(List<String> userIds) {
        List<String> keys = userIds.stream().map(this::serverCacheKey).collect(Collectors.toList());
        List<String> existingKeys = remoteCacheService.existingKeys(keys);
        List<String> onlineUserIds = userIds.stream().filter((userId) -> existingKeys.contains(serverCacheKey(userId)))
                .collect(Collectors.toList());
        return onlineUserIds;
    }

    /**
     * 当用户终端断开连接，移除与PUSH服务的绑定关系
     *
     * @param userTerminal
     */
    public void removeServerCache(ImUserTerminal userTerminal) {
        String cacheKey = serverCacheKey(userTerminal.getUserId());
        remoteCacheService.hashRemove(cacheKey, userTerminal.getTerminalType().toString());

    }


    /**
     * 获取用户在线的终端信息
     *
     * @param userId
     * @return
     */
    public List<ImUserTerminal> onlineTerminals(String userId) {
        Map<String, String> userConnections = getConnectionInfo(userId);
        return userConnections.entrySet().stream().map(entry -> new ImUserTerminal(userId, ImTerminalType.valueOf(entry.getKey())))
                .collect(Collectors.toList());
    }


    /**
     * 获取用户终端连接的PUSH服务ID
     *
     * @return
     */
    public List<String> connectedServerIds(String userId) {
        Map<String, String> userConnections = remoteCacheService.hashGet(serverCacheKey(userId));
        return userConnections.values().stream().distinct().collect(Collectors.toList());
    }

    public String connectedServerId(String userId) {
        List<String> serverIds = connectedServerIds(userId);
        return CollectionUtil.isNotEmpty(serverIds) ? serverIds.get(0) : null;
    }

    /**
     * 将用户终端和服务绑定
     *
     * @param userTerminal
     */
    public void bindPushServer(ImUserTerminal userTerminal) {
        Environment environment = SpringContextHolder.getBean(Environment.class);
        String serverId = environment.getProperty("server.id");
        if (StringUtils.isEmpty(serverId)) {
            throw new IllegalArgumentException("server.id is null");
        }
        String cacheKey = serverCacheKey(userTerminal.getUserId());
        remoteCacheService.hashPut(cacheKey, userTerminal.getTerminalType().toString(),
                serverId, ImConstants.ONLINE_TIMEOUT_SECONDS, TimeUnit.SECONDS);
    }


    /**
     * 获取用户连接信息,key是用户终端类型 value是服务器ID
     *
     * @param userId
     * @return
     */
    public Map<String, String> getConnectionInfo(String userId) {
        Map<String, String> map = remoteCacheService.hashGet(serverCacheKey(userId));
        return map;
    }


    /**
     * 获取缓存中的用户连接的PUSH服务的key
     *
     * @param userId
     * @return1
     */
    public String serverCacheKey(String userId) {
        String cacheKey = String.join(ImConstants.REDIS_KEY_SPLIT,
                ImConstants.IM_USER_SERVER_ID, userId);
        return cacheKey;
    }

    public List<String> getUserIdsByGroupId(String groupId) {
        Set<?> membersSet = remoteCacheService.membersSet(ImConstants.GROUP_MEMBERS_PREFIX + groupId);
        return new LinkedList<Object>(membersSet).stream().map(Object::toString).collect(Collectors.toList());
    }


}
