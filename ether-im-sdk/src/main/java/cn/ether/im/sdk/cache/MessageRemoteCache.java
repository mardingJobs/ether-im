package cn.ether.im.sdk.cache;

import cn.ether.im.common.cache.RemoteCacheService;
import cn.ether.im.common.model.message.ImMessage;
import cn.hutool.core.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * * @Author: Martin(微信：martin-jobs)
 * * @Date    2024/9/29 19:03
 * * @Description
 * * @Github <a href="https://github.com/mardingJobs">Github链接</a>
 **/
@Slf4j
@Component
public class MessageRemoteCache {

    @Resource
    private RemoteCacheService cacheService;

    /**
     * 添加对话消息缓存
     *
     * @param message
     */
    public void putMessage(ImMessage message) {
        Map<String, Object> map = BeanUtil.beanToMap(message, false, true);
        String key = "im_message:" + message.getId();
        cacheService.hashPut(key, map, 5, TimeUnit.MINUTES);
    }

    public void removeMessage(Long messageId) {
        String key = "im_message:" + messageId;
        cacheService.delete(key);
    }

}
