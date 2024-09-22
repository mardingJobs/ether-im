package cn.ether.im.common.enums;

/**
 * * @Author: Martin
 * * @Date    2024/9/17 18:33
 * * @Description
 **/
public enum ImMessageEventType {

    SENT("已发送", ImChatMessageStatus.SENT, 0),
    PUSHED("已推送", ImChatMessageStatus.PUSHED, 2),
    REACHED("已触达", ImChatMessageStatus.REACHED, 4),
    READ("已读", ImChatMessageStatus.READ, 8),
    WITH_DRAWN("已撤回", ImChatMessageStatus.WITH_DRAWN, 16);

    private final String desc;
    /**
     * 事件发生后的消息状态
     */
    private final ImChatMessageStatus nextStatus;
    /**
     * 事件发生的时间顺序
     */
    private final Integer order;

    ImMessageEventType(String desc, ImChatMessageStatus nextStatus, Integer order) {
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

    public ImChatMessageStatus getNextStatus() {
        return nextStatus;
    }
}
