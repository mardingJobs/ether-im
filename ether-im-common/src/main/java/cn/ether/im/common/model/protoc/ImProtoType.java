package cn.ether.im.common.model.protoc;

import java.util.Objects;

/**
 * * @Author: Martin(微信：martin-jobs)
 * * @Date    2024/9/30 03:05
 * * @Description
 * * @Github <a href="https://github.com/mardingJobs">Github链接</a>
 **/
public enum ImProtoType {

    JSON(0),
    PROTOC_BUFFER(1);

    private final Integer code;

    ImProtoType(Integer code) {
        this.code = code;
    }

    public static ImProtoType getByCode(Integer code) {
        for (ImProtoType type : values()) {
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
