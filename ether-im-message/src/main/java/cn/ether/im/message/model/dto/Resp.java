package cn.ether.im.message.model.dto;

import cn.ether.im.common.enums.ImExceptionCode;
import cn.ether.im.common.exception.ImException;
import lombok.Data;

/**
 * * @Author: Martin
 * * @Date    2024/9/15 15:10
 * * @Description
 **/
@Data
public class Resp {

    private Integer code;
    private String msg;
    private Object data;


    // success 方法
    public static Resp success(Object data) {
        Resp resp = new Resp();
        resp.setCode(ImExceptionCode.SUCCESS.getCode());
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
        resp.setCode(ImExceptionCode.SUCCESS.getCode());
        resp.setMsg(msg);
        return resp;
    }

    public static Resp fail(ImException exception) {
        ImExceptionCode exceptionCode = exception.getExceptionCode();
        Resp resp = new Resp();
        resp.setCode(exceptionCode.getCode());
        resp.setMsg(exceptionCode.getMsg());
        return resp;
    }

}
