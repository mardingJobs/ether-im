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
package cn.ether.im.common.cache;

import cn.hutool.json.JSONUtil;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public interface DistributedCacheService {

    /**
     * 永久缓存
     * @param key 缓存key
     * @param value 缓存value
     */
    void set(String key, Object value);

    /**
     * 将数据缓存一段时间
     * @param key 缓存key
     * @param value 缓存value
     * @param timeout 物理缓存的时长
     * @param unit 物理时间单位
     */
    void set(String key, Object value, Long timeout, TimeUnit unit);

    /**
     * 设置缓存过期
     * @param key 缓存key
     * @param timeout 过期时长
     * @param unit 时间单位
     * @return 设置过期时间是否成功
     */
    Boolean expire(String key, final long timeout, final TimeUnit unit);

    /**
     * 向Set中添加元素
     * @param key 缓存的key
     * @param values 缓存的value集合
     */
    Long addSet(String key, String ... values);

    /**
     * 检测Value是否是key set的成员
     * @param key 缓存的key
     * @param value 要检测的元素
     * @return true：是；false：否
     */
    Boolean isMemberSet(String key, Object value);

    /**
     * 获取指定的Set集合下的所有元素
     * @param key 缓存的key
     * @return 当前key下的所有元素
     */
    Set<String> membersSet(String key);

    /**
     * 移除Set集合中的元素
     * @param key 缓存的key
     * @param values 要移除的value
     */
    Long removeSet(String key, Object ... values);

    /**
     * 获取Set集合中的大小
     * @param key 缓存的key
     * @return Set集合大小
     */
    Long sizeSet(String key);

    /**
     * 添加hash缓存
     *
     * @param key
     * @param field
     * @param value
     */
    void hashPut(String key, String field, Object value, long timeout, TimeUnit unit);

    /**
     * 获取hash缓存
     *
     * @param key
     * @return
     */
    Map hashGet(String key);

    /**
     * 获取hash缓存中的field对应的value
     *
     * @param key
     * @param field
     * @return
     */
    Object hashGet(String key, String field);


    /**
     * 获取缓存中的数据
     * @param key 缓存key
     * @return 缓存value
     */
    String get(String key);

    /**
     * 获取缓存数据
     * @param key 缓存的key
     * @param targetClass 目标对象Class
     * @param <T> 泛型
     * @return 返回的数据
     */
    <T> T getObject(String key, Class<T> targetClass);

    /**
     * 根据key列表批量获取value
     * @param keys key列表
     * @return value集合
     */
    List<String> multiGet(Collection<String> keys);

    /**
     * 根据正则表达式获取所有的key集合
     * @param pattern 正则表达式
     * @return key的集合
     */
    Set<String> keys(String pattern);

    /**
     * 删除指定的key
     * @param key key
     * @return 删除是否成功
     */
    Boolean delete(String key);


    /**
     * 获取要保存到缓存中的value字符串，可能是简单类型，也可能是对象类型，也可能是集合数组等
     * @param value 要保存的value值
     * @return 处理好的字符串
     */
    default String getValue(Object value){
        return TypeConversion.isSimpleType(value) ? String.valueOf(value) : JSONUtil.toJsonStr(value);
    }





}
