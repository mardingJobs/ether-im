package cn.ether.im.common.helper;

import cn.ether.im.common.constants.ImConstants;
import cn.ether.im.common.model.ImUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * * @Author: Martin
 * * @Date    2024/9/15 02:10
 * * @Description
 **/
@Component
public class ImMqHelper {

    @Autowired
    private ImCacheHelper cacheHelper;


    public String getTopicByUser(ImUser user) {
        String serverId = cacheHelper.getServerId(user);
        return String.join(ImConstants.MQ_MESSAGE_KEY_SPLIT, user.getGroup(),
                ImConstants.IM_MESSAGE_PERSONAL, String.valueOf(serverId));
    }

}
