package cn.ether.im.common.model.message;

import cn.ether.im.common.enums.ImSystemMessageType;
import lombok.Data;

/**
 * * @Author: Martin
 * * @Date    2024/9/16 00:03
 * * @Description
 **/
@Data
public class ImSystemAckMessage extends ImSystemMessage {

    public ImSystemAckMessage() {
        this.setType(ImSystemMessageType.ACK);
    }

    public ImSystemAckMessage(String seq) {
        this.setType(ImSystemMessageType.ACK);
        this.setSeq(seq);
    }
}