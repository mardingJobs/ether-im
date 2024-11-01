package cn.ether.im.common.model.info.sys;

import cn.ether.im.common.model.protoc.ImProtoType;
import lombok.Data;

/**
 * * @Author: Martin(微信：martin-jobs)
 * * @Date    2024/9/30 23:31
 * * @Description
 * * @Github <a href="https://github.com/mardingJobs">Github链接</a>
 **/
@Data
public class ImProtocolSelectResult {

    private ImProtoType type;

    private char version;


    public static ImProtocolSelectResult defaultProtoc() {
        ImProtocolSelectResult selectResult = new ImProtocolSelectResult();
        selectResult.setType(ImProtoType.JSON);
        return null;
    }
}
