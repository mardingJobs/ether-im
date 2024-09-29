package cn.ether.im.common.model.info;

import cn.ether.im.common.enums.ImSysMessageType;
import cn.ether.im.common.model.info.sys.ImSysMessage;
import lombok.Data;

/**
 * * @Author: Martin
 * * @Date    2024/9/16 00:03
 * * @Description
 **/
@Data
public class ImSysAckMessage extends ImSysMessage {

    public ImSysAckMessage() {
        this.setSystemMessageType(ImSysMessageType.ACK);
    }

    public ImSysAckMessage(String seq) {
        this.setSystemMessageType(ImSysMessageType.ACK);
        this.setUid(seq);
    }
}
