package cn.ether.im.common.model.protoc;

/**
 * * @Author: Martin(微信：martin-jobs)
 * * @Date    2024/9/30 03:05
 * * @Description
 * * @Github <a href="https://github.com/mardingJobs">Github链接</a>
 **/
public enum ImProtocType {

    JSON('0'),
    PROTOBUF('1');

    private final char code;

    ImProtocType(char code) {
        this.code = code;
    }

    public static ImProtocType getByCode(char code) {
        for (ImProtocType type : values()) {
            if (type.code == code) {
                return type;
            }
        }
        return null;
    }

    public char getCode() {
        return code;
    }

}
