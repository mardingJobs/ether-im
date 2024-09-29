package cn.ether.im.common.model.info;

import cn.ether.im.common.enums.ImInfoType;
import cn.ether.im.common.model.info.message.ImMessage;
import cn.ether.im.common.model.info.sys.ImSysMessage;
import com.alibaba.fastjson.JSON;
import lombok.Data;

/**
 * * @Author: Martin(微信：martin-jobs)
 * * @Date    2024/9/18 16:23
 * * @Description
 * * @Github https://github.com/mardingJobs
 **/
@Data
public class ImInfo implements IdentifiableInfo {

    /**
     * 表示消息的唯一标识 (可选)
     * 对于对话消息，是消息ID；对于系统消息，是可以不是消息ID
     */
    private String uid;

    private ImInfoType infoType;

    public static ImInfo parseObject(String json) {
        ImInfo message = JSON.parseObject(json, ImInfo.class);
        ImInfoType infoType = message.getInfoType();
        if (infoType == ImInfoType.MESSAGE) {
            return JSON.parseObject(json, ImMessage.class);
        } else if (infoType == ImInfoType.SYSTEM) {
            return ImSysMessage.parseObject(json);
        }
        throw new IllegalArgumentException("不支持的消息类型:" + infoType);
    }

    public String uid() {
        return uid;
    }

}
