package cn.ether.im.common.enums;

/**
 * * @Author: Martin(微信：martin-jobs)
 * * @Date    2024/9/22 01:23
 * * @Description
 * * @Github <a href="https://github.com/mardingJobs">Github链接</a>
 **/
public enum ImExceptionCode {

    SUCCESS(200, "成功"),
    UN_LOGGED(400, "未登陆"),
    INVALID_TOKEN(401, "token无效或已过期"),
    PARAMS_ERROR(402, "参数错误"),
    PROGRAM_ERROR(500, "系统繁忙，请稍后再试"),
    MESSAGE_DUPLICATION(1001, "消息重复"),
    MESSAGE_PERSIST_FAIL(1002, "消息持久化失败"),
    PUBLISH_MESSAGE_EVENT_FAIL(1003, "发布消息事件失败"),
    NO_MESSAGE_RECEIVER(1004, "没有消息接收人"),
    MESSAGE_SENT_TO_MQ_FAIL(1007, "发送到MQ失败"),
    SEND_MESSAGE_FAIL(1010, "发送消息失败"),
    CONVERSATION_NOT_EXIST(1011, "会话不存在"),
    RECEIVER_NOT_ONLINE(1008, "接受人不在线");


    private Integer code;
    private String msg;

    ImExceptionCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }


}
