package cn.ether.im.common.model.info;

import cn.ether.im.common.model.user.ImUserTerminal;
import lombok.Data;

/**
 * 消息已接受通知
 * * @Author: Martin(微信：martin-jobs)
 * * @Date    2024/10/1 14:24
 * * @Description
 * * @Github <a href="https://github.com/mardingJobs">Github链接</a>
 **/
@Data
public class MessageReceivedNotice extends ImInfo {

    private Long messageId;

    private ImUserTerminal receiverTerminal;
}
