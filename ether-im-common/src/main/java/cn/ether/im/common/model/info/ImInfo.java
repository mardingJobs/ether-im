package cn.ether.im.common.model.info;

import cn.ether.im.common.enums.ImInfoType;
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

    public ImInfo(ImInfoType type) {
        this.type = type;
    }

    public ImInfo(String info) {
        this.info = info;
        this.type = ImInfoType.INFO;
    }

    public ImInfo(ImInfoType type, String info) {
        this.type = type;
        this.info = info;
    }
}
