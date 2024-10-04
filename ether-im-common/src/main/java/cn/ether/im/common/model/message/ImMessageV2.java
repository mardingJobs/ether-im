package cn.ether.im.common.model.message;

import cn.ether.im.common.enums.ImMessageType;

import java.io.Serializable;

/**
 * * @Author: Martin(微信：martin-jobs)
 * * @Date    2024/10/4 21:50
 * * @Description
 * * @Github <a href="https://github.com/mardingJobs">Github链接</a>
 **/
public interface ImMessageV2 extends Serializable {


    ImMessageType getMessageType();

}
