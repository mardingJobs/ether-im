package cn.ether.im.common.util;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * * @Author: Martin
 * * @Date    2024/9/15 21:12
 * * @Description
 **/
@Component
public class SnowflakeUtil {

    @Value("${snowflake.workerId:1}")
    private long workerId;

    @Value("${snowflake.dataCenterId:1}")
    private long dataCenterId;

    private Snowflake snowflake;

    @PostConstruct
    public void init() {
        snowflake = IdUtil.createSnowflake(workerId, dataCenterId);
    }


    public String nextId() {
        return snowflake.nextIdStr();
    }


}
