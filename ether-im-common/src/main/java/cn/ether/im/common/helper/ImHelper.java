package cn.ether.im.common.helper;

import cn.ether.im.common.constants.ImConstants;
import cn.ether.im.common.model.user.ImUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * * @Author: Martin
 * * @Date    2024/9/15 02:10
 * * @Description
 **/
@Component
public class ImHelper {

    @Autowired
    private ImCacheHelper cacheHelper;

    /**
     * 返回用户连接的push服务订阅的topic
     *
     * @param user
     * @return
     */
    public List<String> relatedTopic(ImUser user) {
        List<String> serverIds = cacheHelper.connectedServerIds(user);
        return serverIds.stream().map(serverId -> String.join(ImConstants.MQ_MESSAGE_KEY_SPLIT, user.getGroup(),
                ImConstants.IM_MESSAGE_PERSONAL, serverId)).collect(Collectors.toList());
    }

}
