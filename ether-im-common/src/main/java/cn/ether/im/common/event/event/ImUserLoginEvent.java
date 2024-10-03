package cn.ether.im.common.event.event;

import cn.ether.im.common.enums.ImTerminalType;
import lombok.Data;

import java.io.Serializable;

/**
 * * @Author: Martin(微信：martin-jobs)
 * * @Date    2024/10/3 16:18
 * * @Description
 * * @Github <a href="https://github.com/mardingJobs">Github链接</a>
 **/
@Data
public class ImUserLoginEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    private String userId;

    private ImTerminalType terminalType;

    /**
     * 登陆时间
     */
    private Long loginTime;

}
