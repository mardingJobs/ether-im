package cn.ether.im.common.model.protoc;

import cn.ether.im.common.model.info.ImInfo;
import lombok.Data;

import java.io.Serializable;

/**
 * * 头：协议类型code(json/protobuf) 消息类型code
 * *
 * * 体：json/protobuf的消息体
 * * @Author: Martin(微信：martin-jobs)
 * * @Date    2024/9/30 01:45
 * * @Description
 * * @Github <a href="https://github.com/mardingJobs">Github链接</a>
 **/
@Data
public class ImProtoc implements Serializable {

    private static final long serialVersionUID = -7962158433664656629L;

    private ImProtocType type;

    private char version;

    private ImInfo body;

}
