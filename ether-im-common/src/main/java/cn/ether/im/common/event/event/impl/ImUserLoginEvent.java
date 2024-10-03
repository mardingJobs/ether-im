package cn.ether.im.common.event.event.impl;

import cn.ether.im.common.enums.ImTerminalType;
import cn.ether.im.common.event.event.ImEvent;
import cn.ether.im.common.event.event.ImEventType;
import lombok.Data;

import java.io.Serializable;

/**
 * * @Author: Martin(微信：martin-jobs)
 * * @Date    2024/10/3 16:18
 * * @Description
 * * @Github <a href="https://github.com/mardingJobs">Github链接</a>
 **/
@Data
public class ImUserLoginEvent implements ImEvent, Serializable {

    private static final long serialVersionUID = 1L;

    private String userId;

    private ImTerminalType terminalType;

    /**
     * 登陆时间
     */
    private Long loginTime;

    @Override
    public ImEventType getEventType() {
        return ImEventType.USER_LOGIN;
    }
}
