package cn.ether.im.common.event;

import cn.ether.im.common.enums.ImChatMessageStatus;
import cn.ether.im.common.model.info.message.event.ImMessageEventType;
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
    private static final Map<ImmutablePair<ImChatMessageStatus, ImMessageEventType>, ImChatMessageStatus> statusContext = new HashMap<>();

    static {
//        put(ImMessageStatus.INTI, ImMessageEventType.SENT);
        put(ImChatMessageStatus.INTI, ImMessageEventType.WITH_DRAWN);
        put(ImChatMessageStatus.SENT, ImMessageEventType.REACHED);
        put(ImChatMessageStatus.SENT, ImMessageEventType.WITH_DRAWN);
        put(ImChatMessageStatus.REACHED, ImMessageEventType.READ);
        put(ImChatMessageStatus.REACHED, ImMessageEventType.WITH_DRAWN);
        put(ImChatMessageStatus.READ, ImMessageEventType.WITH_DRAWN);
    }

    public static void put(ImChatMessageStatus originalStatus, ImMessageEventType event) {
        statusContext.put(ImmutablePair.of(originalStatus, event), event.getNextStatus());
    }


    public static ImChatMessageStatus nextStatus(ImChatMessageStatus status, ImMessageEventType event) {
        return statusContext.get(ImmutablePair.of(status, event));
    }

}
