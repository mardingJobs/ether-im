package cn.ether.im.common.enums;

/**
 * * @Author: Martin
 * * @Date    2024/9/17 18:33
 * * @Description
 **/
public enum ImMessageEventType {

    SENT("已发送", ImMessageStatus.SENT, 0),
    PUSHED("已推送", ImMessageStatus.PUSHED, 2),
    REACHED("已触达", ImMessageStatus.REACHED, 4),
    READ("已读", ImMessageStatus.READ, 8),
    WITH_DRAWN("已撤回", ImMessageStatus.WITH_DRAWN, 16);

    private final String desc;
    /**
     * 事件发生后的消息状态
     */
    private final ImMessageStatus nextStatus;
    /**
     * 事件发生的时间顺序
     */
    private final Integer order;

    ImMessageEventType(String desc, ImMessageStatus nextStatus, Integer order) {
        this.desc = desc;
        this.nextStatus = nextStatus;
        this.order = order;
    }

    public Integer getOrder() {
        return order;
    }

    public String getDesc() {
        return desc;
    }

    public ImMessageStatus getNextStatus() {
        return nextStatus;
    }
}
