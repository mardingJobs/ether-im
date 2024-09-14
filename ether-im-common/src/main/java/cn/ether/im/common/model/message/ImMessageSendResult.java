package cn.ether.im.common.model.message;

import cn.ether.im.common.enums.MessageSendCode;
import lombok.Data;

/**
 * 消息发送结果
 * * @Author: Martin
 * * @Date    2024/9/14 18:54
 * * @Description
 **/
@Data
public class ImMessageSendResult {

    /**
     * 消息
     */
    private ImMessage message;

    /**
     * 发送结果
     */
    private MessageSendCode sendCode;

}
