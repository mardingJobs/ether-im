package cn.ether.im.push.mq;

import cn.ether.im.common.constants.ImConstants;
import cn.ether.im.common.model.info.ImTopicInfo;
import cn.ether.im.common.model.info.message.event.ImMessageEvent;
import cn.ether.im.common.mq.ImMqMessageSender;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * * @Author: Martin
 * * @Date    2024/9/17 22:23
 * * @Description
 **/
@Slf4j
@Component
public class ImMessageEventMQProducer {

    @Resource
    private ImMqMessageSender sender;

    public void publish(ImMessageEvent event) throws Exception {
        ImTopicInfo<ImMessageEvent> topicMessage = new ImTopicInfo<>(event, ImConstants.IM_MESSAGE_EVENT_TOPIC);
        boolean send = sender.send(topicMessage);
        if (send) {
            log.error("消息事件发布失败,MessageEvent:{}", JSON.toJSONString(event));
        }
    }
}
