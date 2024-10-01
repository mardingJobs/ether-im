package cn.ether.im.common.model.protoc;

import java.util.Objects;

/**
 * * @Author: Martin(微信：martin-jobs)
 * * @Date    2024/9/30 03:05
 * * @Description
 * * @Github <a href="https://github.com/mardingJobs">Github链接</a>
 **/
public enum ImProtocType {

    JSON(0),
    PROTOC_BUFFER(1);

    private final Integer code;

    ImProtocType(Integer code) {
        this.code = code;
    }

    public static ImProtocType getByCode(Integer code) {
        for (ImProtocType type : values()) {
            if (Objects.equals(type.code, code)) {
                return type;
            }
        }
        return null;
    }

    public Integer getCode() {
        return code;
    }

}
