package cn.ether.im.common.enums;

/**
 * * @Author: jack
 * * @Date    2024/9/15 20:57
 * * @Description
 **/
public enum ImMessageContentType {

    TEXT(0),
    IMAGE(1),
    VIDEO(2),
    AUDIO(3),
    FILE(4);

    private int code;

    ImMessageContentType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
