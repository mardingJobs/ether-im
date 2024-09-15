package cn.ether.im.common.model.message;

import lombok.Data;

/**
 * * @Author: Martin
 * * @Date    2024/9/16 00:03
 * * @Description
 **/
@Data
public class ImAckMessage implements ChannelMessage {

    private final String ACK_MSG = "ack";

    private String ack = ACK_MSG;

}
