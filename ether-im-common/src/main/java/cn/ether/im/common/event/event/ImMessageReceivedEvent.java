package cn.ether.im.common.event.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 消息已被用户接受，任意终端接受都会触发事件
 * * @Author: Martin(微信：martin-jobs)
 * * @Date    2024/10/3 21:43
 * * @Description
 * * @Github <a href="https://github.com/mardingJobs">Github链接</a>
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImMessageReceivedEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long messageId;

    private String receiverId;


}
