package cn.ether.im.common.event;

import cn.ether.im.common.enums.ImChatMessageStatus;
import cn.ether.im.common.model.info.message.event.ImEventType;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.HashMap;
import java.util.Map;

/**
 * 事件状态机
 * * @Author: Martin(微信：martin-jobs)
 * * @Date    2024/9/18 23:44
 * * @Description
 * * @Github https://github.com/mardingJobs
 **/
public class ImEventStatusMachine {

    /**
     * key: 初始状态+事件
     * value: 改变后的状态
     */
    private static final Map<ImmutablePair<ImChatMessageStatus, ImEventType>, ImChatMessageStatus> statusContext = new HashMap<>();

    static {
//        put(ImMessageStatus.INTI, ImMessageEventType.SENT);
        put(ImChatMessageStatus.INTI, ImEventType.WITH_DRAWN);
        put(ImChatMessageStatus.SENT, ImEventType.RECEIVED);
        put(ImChatMessageStatus.SENT, ImEventType.WITH_DRAWN);
        put(ImChatMessageStatus.RECEIVED, ImEventType.READ);
        put(ImChatMessageStatus.RECEIVED, ImEventType.WITH_DRAWN);
        put(ImChatMessageStatus.READ, ImEventType.WITH_DRAWN);
    }

    public static void put(ImChatMessageStatus originalStatus, ImEventType event) {
        statusContext.put(ImmutablePair.of(originalStatus, event), event.getNextStatus());
    }


    public static ImChatMessageStatus nextStatus(ImChatMessageStatus status, ImEventType event) {
        return statusContext.get(ImmutablePair.of(status, event));
    }

}
