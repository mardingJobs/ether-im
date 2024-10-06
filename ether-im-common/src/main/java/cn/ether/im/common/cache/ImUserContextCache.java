package cn.ether.im.common.cache;


import cn.ether.im.common.constants.ImConstants;
import cn.ether.im.common.enums.ImTerminalType;
import cn.ether.im.common.model.user.ImUser;
import cn.ether.im.common.model.user.ImUserTerminal;
import cn.ether.im.common.util.SpringContextHolder;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.*;
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
     * 判断用户是否在线
     *
     * @param user
     * @return
     */
    public boolean online(ImUser user) {
        return CollectionUtil.isNotEmpty(getConnectionInfo(user));
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


    public String getMessageTag(ImUserTerminal userTerminal) {
        String serverId = this.connectedServerId(userTerminal);
        if (StringUtils.isEmpty(serverId)) {
            return null;
        }
        return ImConstants.IM_CHAT_MESSAGE_TAG_PREFIX + ImConstants.MQ_TOPIC_SPLIT + serverId;
    }


    /**
     * 获取用户在线的终端类型
     *
     * @param userInfo
     * @return
     */
    public List<ImTerminalType> onlineTerminalTypes(ImUser userInfo) {
        Map<String, String> userConnections = getConnectionInfo(userInfo);
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
        Map<String, String> userConnections = getConnectionInfo(userInfo);
        return userConnections.entrySet().stream().map(entry -> new ImUserTerminal(userInfo, ImTerminalType.valueOf(entry.getKey())))
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

    public String connectedServerId(ImUserTerminal userTerminal) {
        Map<String, String> terminalBindedServerIds = getConnectionInfo(userTerminal);
        if (MapUtil.isNotEmpty(terminalBindedServerIds)) {
            return terminalBindedServerIds.get(userTerminal.getTerminalType().name());
        }
        return null;
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
     * @param userInfo
     * @return
     */
    public Map<String, String> getConnectionInfo(ImUser userInfo) {
        Map<String, String> map = remoteCacheService.hashGet(serverCacheKey(userInfo.getUserId()));
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
