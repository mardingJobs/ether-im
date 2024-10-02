package cn.ether.im.proto.text;

import lombok.Data;

/**
 * * @Author: Martin(微信：martin-jobs)
 * * @Date    2024/10/2 14:27
 * * @Description
 * * @Github <a href="https://github.com/mardingJobs">Github链接</a>
 **/
@Data
public class ImTextProto {

    private int type;

    private ImMessageTextProto body;


}
