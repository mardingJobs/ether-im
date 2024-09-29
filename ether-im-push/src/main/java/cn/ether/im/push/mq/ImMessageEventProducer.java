package cn.ether.im.push.mq;

import cn.ether.im.common.constants.ImConstants;
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
public class ImMessageEventProducer {

    @Resource
    private ImMessageSender sender;

    public void publish(ImMessageEvent event) throws Exception {
        ImTopicMessage<ImMessageEvent> topicMessage = new ImTopicMessage<>(event, ImConstants.IM_MESSAGE_EVENT_TOPIC);
        boolean send = sender.sendOrderlyByUid(topicMessage);
        if (send) {
            log.info("消息事件发布成功,MessageEvent:{}" , JSON.toJSONString(event));
        } else {
            log.error("消息事件发布失败,MessageEvent:{}", JSON.toJSONString(event));
        }
    }


}
