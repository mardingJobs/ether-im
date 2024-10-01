package cn.ether.im.common.model.info;

import cn.ether.im.common.enums.ImInfoType;
import com.alibaba.fastjson.JSON;
import lombok.Data;

/**
 * * @Author: Martin(微信：martin-jobs)
 * * @Date    2024/9/18 16:23
 * * @Description
 * * @Github https://github.com/mardingJobs
 **/
@Data
public class ImInfo {

    private ImInfoType type;

    private String info;

    public ImInfo() {
    }

    public ImInfo(String info) {
        this.info = info;
        this.type = ImInfoType.INFO;
    }

    public static ImInfo parseObject(String json) {
        ImInfo message = JSON.parseObject(json, ImInfo.class);
        ImInfoType infoType = message.getType();
        Class<? extends ImInfo> infoClass = infoType.getInfoClass();
        return JSON.parseObject(json, infoClass);
    }

}
