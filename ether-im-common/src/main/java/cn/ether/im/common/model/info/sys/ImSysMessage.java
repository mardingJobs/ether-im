package cn.ether.im.common.model.info.sys;

import cn.ether.im.common.enums.ImInfoType;
import cn.ether.im.common.enums.ImSysMessageType;
import cn.ether.im.common.model.info.ImInfo;
import com.alibaba.fastjson.JSON;
import lombok.Data;

/**
 * * @Author: Martin
 * * @Date    2024/9/16 01:06
 * * @Description
 **/
@Data
public class ImSysMessage extends ImInfo {

    private static final long serialVersionUID = 1L;

    /**
     * 消息类型
     */
    private ImSysMessageType systemMessageType;

    public ImSysMessage() {
        this.setInfoType(ImInfoType.SYSTEM);
    }

    public ImSysMessage(ImSysMessageType systemMessageType) {
        this();
        this.systemMessageType = systemMessageType;
    }

    public static ImSysMessage parseObject(String json) {
        ImSysMessage imSysMessage = JSON.parseObject(json, ImSysMessage.class);
        ImSysMessageType messageType = imSysMessage.getSystemMessageType();
        Class<? extends ImSysMessage> messageClass = messageType.getMessageClass();
        return JSON.parseObject(json, messageClass);
    }
}
