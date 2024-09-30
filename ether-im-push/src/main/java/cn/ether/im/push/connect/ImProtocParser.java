package cn.ether.im.push.connect;

import cn.ether.im.common.model.info.ImInfo;
import cn.ether.im.common.model.protoc.ImProtoc;
import cn.ether.im.common.model.protoc.ImProtocType;
import cn.ether.im.push.exception.ImProtocException;
import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 协议格式
 * 头：
 * 协议类型(两个字节) 版本号(两个字节) 预留4个字节。
 * 体：
 * 消息体(json)/二进制
 * * @Author: Martin(微信：martin-jobs)
 * * @Date    2024/9/30 13:13
 * * @Description
 * * @Github <a href="https://github.com/mardingJobs">Github链接</a>
 **/
@Slf4j
@Data
public class ImProtocParser {

    public static ImProtoc parseText(String text) throws ImProtocException {
        int splitIndex = text.indexOf("\n");
        if (splitIndex < 0) {
            throw new ImProtocException("协议格式错误");
        }
        String header = text.substring(0, splitIndex);
        char type = header.charAt(0);
        char version = header.charAt(1);
        String body = text.substring(splitIndex + 1);
        log.info("解析到消息类型:{},版本号:{},消息体:{}", type, version, body);
        ImProtoc imProtoc = new ImProtoc();
        ImProtocType protocType = ImProtocType.getByCode(type);
        if (protocType == null) {
            throw new ImProtocException("协议格式错误:不支持的协议类型");
        }
        imProtoc.setType(protocType);
        imProtoc.setVersion(version);
        ImInfo imInfo = ImInfo.parseObject(body);
        imProtoc.setBody(imInfo);
        return imProtoc;
    }


    public static String toText(ImProtoc protoc) {
        char type = protoc.getType().getCode();
        char version = protoc.getVersion();
        String body = JSON.toJSONString(protoc.getBody());
        return String.format("%c%c%s", type, version, body);
    }

}
