package cn.ether.im.common.model.message;

import cn.ether.im.common.enums.SystemMessageType;
import lombok.Data;

/**
 * * @Author: Martin
 * * @Date    2024/9/16 00:03
 * * @Description
 **/
@Data
public class ImAckMessage extends ImSystemMessage {

    public ImAckMessage() {
        this.setType(SystemMessageType.ACK_RECEIVE);
    }
}
