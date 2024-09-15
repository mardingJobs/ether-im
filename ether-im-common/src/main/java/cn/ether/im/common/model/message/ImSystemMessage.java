package cn.ether.im.common.model.message;

import cn.ether.im.common.enums.SystemMessageType;
import lombok.Data;

/**
 * * @Author: Martin
 * * @Date    2024/9/16 01:06
 * * @Description
 **/
@Data
public class ImSystemMessage {

    /**
     * 消息类型
     */
    private SystemMessageType type;
}
