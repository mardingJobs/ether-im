package cn.ether.im.common.model.protoc;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 头：协议类型code(json/protobuf) 消息类型code
 * <p>
 * 体：json/protobuf的消息体
 * * @Author: Martin(微信：martin-jobs)
 * * @Date    2024/9/30 01:47
 * * @Description
 * * @Github <a href="https://github.com/mardingJobs">Github链接</a>
 **/
@Data
@AllArgsConstructor
public class ImProtocHeader {

    private ImProtocType type;

}
