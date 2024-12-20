package cn.ether.im.push.processor.flusher;

import cn.ether.im.common.model.message.ImChatMessage;
import cn.ether.im.common.model.user.ImUserTerminal;

/**
 * * @Author: Martin(微信：martin-jobs)
 * * @Date    2024/9/24 13:26
 * * @Description
 * * @Github <a href="https://github.com/mardingJobs">Github链接</a>
 **/
public interface ImMessageFlusher {

    /**
     * 刷新消息到receiverTerminal终端
     *
     * @param receiverTerminal
     * @param message
     */
    boolean flush(ImUserTerminal receiverTerminal, ImChatMessage message);
}
