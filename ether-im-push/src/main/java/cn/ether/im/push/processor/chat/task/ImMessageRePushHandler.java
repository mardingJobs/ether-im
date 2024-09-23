package cn.ether.im.push.processor.chat.task;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.DelayQueue;

/**
 * * @Author: Martin(微信：martin-jobs)
 * * @Date    2024/9/23 15:49
 * * @Description
 * * @Github <a href="https://github.com/mardingJobs">Github链接</a>
 **/
public class ImMessageRePushHandler {

    private Map<Long, ImMessageRePushTask> rePushTaskMap = new HashMap<>();

    private DelayQueue<ImMessageRePushTask> delayQueue = new DelayQueue<>();


    public void addRePushTask(ImMessageRePushTask task) {
        rePushTaskMap.put(task.getMessage().getId(), task);
        delayQueue.add(task);
    }


}
