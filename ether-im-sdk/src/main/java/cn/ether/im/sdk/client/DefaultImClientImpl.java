package cn.ether.im.sdk.client;

import cn.ether.im.common.model.message.ImGroupMessage;
import cn.ether.im.common.model.message.ImPersonalMessage;
import cn.ether.im.common.util.SnowflakeUtil;
import cn.ether.im.sdk.sender.ChatMessageSender;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * * @Author: Martin
 * * @Date    2024/9/15 15:19
 * * @Description
 **/
@Component
public class DefaultImClientImpl implements EtherImClient {


    @Resource
    private ChatMessageSender messageSender;

    @Resource
    private SnowflakeUtil snowflakeUtil;


    /**
     * 发送单聊消息
     *
     * @param personalMessage
     */
    @Override
    public String sendPersonalMessage(ImPersonalMessage personalMessage) {
        // 设置消息ID
        if (StringUtils.isEmpty(personalMessage.getId())) {
            String id = snowflakeUtil.nextId();
            personalMessage.setId(id);
        }
        // 设置时间
        if (personalMessage.getTimestamp() == null) {
            personalMessage.setTimestamp(System.currentTimeMillis());
        }
        messageSender.sendPersonalMessage(personalMessage);
        return personalMessage.getId();
    }

    /**
     * 发送群聊消息
     *
     * @param groupMessage
     */
    @Override
    public String sendGroupMessage(ImGroupMessage groupMessage) {
        return null;
    }
}
