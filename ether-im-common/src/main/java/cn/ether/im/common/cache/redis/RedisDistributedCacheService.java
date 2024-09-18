/**
 * Copyright 2022-9999 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.ether.im.common.cache.redis;

import cn.ether.im.common.cache.DistributedCacheService;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;


@Component
@ConditionalOnProperty(name = "distribute.cache.type", havingValue = "redis", matchIfMissing = true)
public class RedisDistributedCacheService implements DistributedCacheService {

    private final Logger logger = LoggerFactory.getLogger(RedisDistributedCacheService.class);

    //缓存空数据的时长，单位秒
    private static final Long CACHE_NULL_TTL = 60L;
    //缓存的空数据
    private static final String EMPTY_VALUE = "";
    //缓存的空列表数据
    private static final String EMPTY_LIST_VALUE = "[]";
    //分布式锁key的后缀
    private static final String LOCK_SUFFIX = "_lock";
    //线程休眠的毫秒数
    private static final long THREAD_SLEEP_MILLISECONDS = 50;

    @Autowired
    @Qualifier("stringRedisTemplate")
    private StringRedisTemplate redisTemplate;

    @Override
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, this.getValue(value));
    }

    @Override
    public void set(String key, Object value, Long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, this.getValue(value), timeout, unit);
    }

    @Override
    public Boolean expire(String key, long timeout, TimeUnit unit) {
        return redisTemplate.expire(key, timeout, unit);
    }

    @Override
    public Long addSet(String key, String... values) {
        return redisTemplate.opsForSet().add(key, values);
    }

    @Override
    public Boolean isMemberSet(String key, Object value) {
        return redisTemplate.opsForSet().isMember(key, value);
    }

    @Override
    public Set<String> membersSet(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    @Override
    public Long removeSet(String key, Object... values) {
        return redisTemplate.opsForSet().remove(key, values);
    }

    @Override
    public Long sizeSet(String key) {
        return redisTemplate.opsForSet().size(key);
    }

    /**
     * @param key
     * @param field
     * @param value
     */
    @Override
    public void hashPut(String key, String field, Object value, long timeout, TimeUnit unit) {
        redisTemplate.opsForHash().put(key, field, this.getValue(value));
        expire(key, timeout, unit);
    }

    /**
     * @param key
     * @param field
     */
    @Override
    public void hashRemove(String key, String field) {
        redisTemplate.opsForHash().delete(key, field);
    }

    /**
     * @param key
     * @return
     */
    @Override
    public Map hashGet(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 获取hash缓存中的field对应的value
     *
     * @param key
     * @param field
     * @return
     */
    @Override
    public Object hashGet(String key, String field) {
        return redisTemplate.opsForHash().get(key, field);
    }


    @Override
    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public <T> T getObject(String key, Class<T> targetClass) {
        Object result = redisTemplate.opsForValue().get(key);
        if (result == null) {
            return null;
        }
        try {
            return JSONUtil.toBean(result.toString(), targetClass);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<String> multiGet(Collection<String> keys) {
        return redisTemplate.opsForValue().multiGet(keys);
    }

    @Override
    public Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    @Override
    public Boolean delete(String key) {
        if (StrUtil.isEmpty(key)) {
            return false;
        }
        return redisTemplate.delete(key);
    }
}

