package cn.ether.im.common.enums;

/**
 * * @Author: Martin
 * * @Date    2024/9/17 18:33
 * * @Description
 **/
public enum ImMessageEventType {

    PUSHED("已推送", ImMessageStatus.PUSHED, 0),
    REACHED("已触达", ImMessageStatus.REACHED, 1),
    READ("已读", ImMessageStatus.READ, 2),
    WITH_DRAWN("已撤回", ImMessageStatus.WITH_DRAWN, 9);

    private final String desc;
    /**
     * 事件发生后的消息状态
     */
    private final ImMessageStatus status;
    /**
     * 事件发生的时间顺序
     */
    private final Integer order;

    ImMessageEventType(String desc, ImMessageStatus status, Integer order) {
        this.desc = desc;
        this.status = status;
        this.order = order;
    }

    public Integer getOrder() {
        return order;
    }

    public String getDesc() {
        return desc;
    }

    public ImMessageStatus getStatus() {
        return status;
    }
}
