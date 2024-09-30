package cn.ether.im.push.config;

import cn.ether.im.common.enums.ImInfoType;
import cn.ether.im.common.model.info.ImInfo;
import cn.ether.im.push.processor.ImInfoProcessor;
import cn.hutool.core.map.MapUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * * @Author: Martin(微信：martin-jobs)
 * * @Date    2024/9/30 21:52
 * * @Description
 * * @Github <a href="https://github.com/mardingJobs">Github链接</a>
 **/
@Configuration
public class ImInfoConfig {

    @Bean
    public Map<ImInfoType, ImInfoProcessor<? extends ImInfo>> mapping(List<ImInfoProcessor<? extends ImInfo>> processors) {
        HashMap<ImInfoType, ImInfoProcessor<? extends ImInfo>> map = MapUtil.newHashMap();
        for (ImInfoProcessor<? extends ImInfo> processor : processors) {
            map.put(processor.supportType(), processor);
        }
        return map;
    }

}
