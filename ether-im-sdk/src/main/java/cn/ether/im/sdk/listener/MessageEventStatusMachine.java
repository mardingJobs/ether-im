package cn.ether.im.sdk.listener;

import cn.ether.im.common.enums.ImMessageEventType;
import cn.ether.im.common.enums.ImMessageStatus;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.HashMap;
import java.util.Map;

/**
 * 消息事件状态机
 * * @Author: Martin(微信：martin-jobs)
 * * @Date    2024/9/18 23:44
 * * @Description
 * * @Github https://github.com/mardingJobs
 **/
public class MessageEventStatusMachine {

    /**
     * key: 初始状态+事件
     * value: 改变后的状态
     */
    private static final Map<ImmutablePair<ImMessageStatus, ImMessageEventType>, ImMessageStatus> statusContext = new HashMap<>();

    static {
//        put(ImMessageStatus.INTI, ImMessageEventType.SENT);
        put(ImMessageStatus.INTI, ImMessageEventType.WITH_DRAWN);
        put(ImMessageStatus.SENT, ImMessageEventType.PUSHED);
        put(ImMessageStatus.SENT, ImMessageEventType.WITH_DRAWN);
        put(ImMessageStatus.PUSHED, ImMessageEventType.REACHED);
        put(ImMessageStatus.PUSHED, ImMessageEventType.WITH_DRAWN);
        put(ImMessageStatus.REACHED, ImMessageEventType.READ);
        put(ImMessageStatus.REACHED, ImMessageEventType.WITH_DRAWN);
        put(ImMessageStatus.READ, ImMessageEventType.WITH_DRAWN);
    }

    public static void put(ImMessageStatus originalStatus, ImMessageEventType event) {
        statusContext.put(ImmutablePair.of(originalStatus, event), event.getNextStatus());
    }


    public static ImMessageStatus nextStatus(ImMessageStatus status, ImMessageEventType event) {
        return statusContext.get(ImmutablePair.of(status, event));
    }

}
