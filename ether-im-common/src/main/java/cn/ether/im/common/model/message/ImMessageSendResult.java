package cn.ether.im.common.model.message;

import cn.ether.im.common.constants.ImConstants;
import cn.ether.im.common.enums.MessageSendCode;
import lombok.Data;

/**
 * 消息发送结果
 * * @Author: Martin
 * * @Date    2024/9/14 18:54
 * * @Description
 **/
@Data
public class ImMessageSendResult extends ImTopicMessage {

    /**
     * 发送结果
     */
    private MessageSendCode sendCode;


    public ImMessageSendResult() {
        this.setTopic(ImConstants.TOPIC_IM_RESULT);
    }
}
