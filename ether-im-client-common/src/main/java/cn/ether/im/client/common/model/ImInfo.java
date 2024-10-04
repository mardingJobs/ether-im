package cn.ether.im.client.common.model;

import cn.ether.im.client.common.enums.ImInfoType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * * @Author: Martin(微信：martin-jobs)
 * * @Date    2024/9/18 16:23
 * * @Description
 * * @Github https://github.com/mardingJobs
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImInfo<T> {

    private ImInfoType type;

    private T info;

    public ImInfo(ImInfoType type) {
        this.type = type;
    }

    public ImInfo(T info) {
        this.info = info;
        this.type = ImInfoType.COMMON;
    }

}
