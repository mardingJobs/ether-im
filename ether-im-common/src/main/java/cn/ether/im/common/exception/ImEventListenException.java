package cn.ether.im.common.exception;

/**
 * * @Author: Martin(微信：martin-jobs)
 * * @Date    2024/10/5 18:56
 * * @Description
 * * @Github <a href="https://github.com/mardingJobs">Github链接</a>
 **/
public class ImEventListenException extends RuntimeException {

    public ImEventListenException(String message) {
        super(message);
    }

    public ImEventListenException(Throwable cause) {
        super(cause);
    }

    public ImEventListenException(String message, Throwable cause) {
        super(message, cause);
    }
}
