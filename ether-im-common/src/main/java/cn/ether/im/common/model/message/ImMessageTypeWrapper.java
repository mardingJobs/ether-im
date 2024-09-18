package cn.ether.im.common.model.message;

import cn.ether.im.common.enums.ImMessageTypeEnum;
import lombok.Data;

/**
 * * @Author: Martin
 * * @Date    2024/9/16 01:03
 * * @Description
 **/
@Data
public class ImMessageTypeWrapper {

    /**
     * 消息类型
     */
    private ImMessageTypeEnum type = ImMessageTypeEnum.CHAT;

}
