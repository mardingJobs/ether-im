package cn.ether.im.push.mq;

import cn.ether.im.common.constants.ImConstants;
import cn.ether.im.common.event.ImMessageEventType;
import cn.ether.im.common.event.listener.ImEventListener;
import cn.ether.im.common.event.listener.ImMessageEventListener;
import cn.ether.im.common.model.message.ImMessageEvent;
import cn.ether.im.common.model.message.ImTopicMessage;
import cn.ether.im.common.mq.ImMessageSender;
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
@ImEventListener(types = {ImMessageEventType.REACHED, ImMessageEventType.READ, ImMessageEventType.WITH_DRAWN})
public class ImMessageEventDefaultMqListener implements ImMessageEventListener {

    @Resource
    private ImMessageSender sender;

    public void publish(ImMessageEvent event) throws Exception {
        ImTopicMessage<ImMessageEvent> topicMessage = new ImTopicMessage<>(event, ImConstants.IM_MESSAGE_EVENT_TOPIC);
        boolean send = sender.send(topicMessage);
        if (send) {
            log.error("消息事件发布失败,MessageEvent:{}", JSON.toJSONString(event));
        }
    }


    @Override
    public void onMessageEvent(ImMessageEvent messageEvent) throws Exception {
        publish(messageEvent);
    }
}
