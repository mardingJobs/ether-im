package cn.ether.im.common.event.event;

import java.io.Serializable;

/**
 * * @Author: Martin(微信：martin-jobs)
 * * @Date    2024/10/4 00:22
 * * @Description
 * * @Github <a href="https://github.com/mardingJobs">Github链接</a>
 **/
public interface ImEvent extends Serializable {


    ImEventType getEventType();

}
