package cn.ether.im.common.exception;

import cn.ether.im.common.enums.ImExceptionCode;
import lombok.Data;

/**
 * * @Author: Martin
 * * @Date    2024/9/17 17:05
 * * @Description
 **/
@Data
public class ImException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private ImExceptionCode exceptionCode;

    public ImException(ImExceptionCode exceptionCode) {
        super(exceptionCode.getMsg());
        this.exceptionCode = exceptionCode;
    }
}
