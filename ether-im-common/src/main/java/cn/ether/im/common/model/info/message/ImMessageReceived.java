package cn.ether.im.common.model.info.message;

import cn.ether.im.common.model.info.ImInfo;
import lombok.Data;

/**
 * * @Author: Martin(微信：martin-jobs)
 * * @Date    2024/10/3 22:18
 * * @Description
 * * @Github <a href="https://github.com/mardingJobs">Github链接</a>
 **/
@Data
public class ImMessageReceived extends ImInfo {

    private Long messageId;

}
