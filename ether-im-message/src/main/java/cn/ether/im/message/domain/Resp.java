package cn.ether.im.message.domain;

import lombok.Data;

/**
 * * @Author: Martin
 * * @Date    2024/9/15 15:10
 * * @Description
 **/
@Data
public class Resp {

    private String code;
    private String msg;
    private Object data;


    // success 方法
    public static Resp success(Object data) {
        Resp resp = new Resp();
        resp.setCode("200");
        resp.setMsg("success");
        resp.setData(data);
        return resp;
    }

    // success
    public static Resp success() {
        return success(null);
    }

    // fail
    public static Resp fail(String msg) {
        Resp resp = new Resp();
        resp.setCode("500");
        resp.setMsg(msg);
        return resp;
    }

}
