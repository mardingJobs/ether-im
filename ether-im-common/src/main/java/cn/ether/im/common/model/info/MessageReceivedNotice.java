package cn.ether.im.common.model.info;

import cn.ether.im.common.enums.ImInfoType;
import cn.ether.im.common.enums.ImTerminalType;
import lombok.Data;

/**
 * 终端已收到消息通知
 * * @Author: Martin(微信：martin-jobs)
 * * @Date    2024/10/1 14:24
 * * @Description
 * * @Github <a href="https://github.com/mardingJobs">Github链接</a>
 **/
@Data
public class MessageReceivedNotice extends ImInfo {

    private Long messageId;

    private String userId;

    private ImTerminalType terminalType;

    public MessageReceivedNotice() {
        super(ImInfoType.MESSAGE_RECEIVED);
    }
}
