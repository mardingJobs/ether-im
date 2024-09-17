package cn.ether.im.common.exception;

/**
 * * @Author: Martin
 * * @Date    2024/9/17 17:05
 * * @Description
 **/
public class ImException extends RuntimeException {

    private static final long serialVersionUID = 1L;


    public ImException(String message) {
        super(message);
    }

    public ImException(String message, Throwable cause) {
        super(message, cause);
    }
}
