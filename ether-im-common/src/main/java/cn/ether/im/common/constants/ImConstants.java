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
package cn.ether.im.common.constants;


public class ImConstants {


    public static final String USER_KEY = "USER";

    /**
     * 默认的分组名称
     */
    public static final String DEFAULT_GROUP_NAME = "default";

    /**
     * Redis Key的分隔符
     */
    public static final String REDIS_KEY_SPLIT = ":";


    /**
     * 在线状态过期时间，默认是1800秒，也就是30分钟
     */
    public static final long ONLINE_TIMEOUT_SECONDS = 1800;


    /**
     * 用户ID所连接的IM-server的ID
     */
    public final static String IM_USER_SERVER_ID = "im:user:server_id";


    public final static String IM_MESSAGE_PUSH_TOPIC = "im-push-topic";


    public final static String IM_MESSAGE_PUSH_CONSUMERS = "im-push-consumer";


    public final static String IM_MESSAGE_EVENT_CONSUMER_GROUP = "im-message-event-consumer";

    /**
     * 心跳次数
     */
    public static final String HEARTBEAT_TIMES = "ht_times";

    public static final String MQ_TOPIC_SPLIT = "-";

    /**
     * 消息事件主题
     */
    public static final String IM_MESSAGE_EVENT_TOPIC = "im_message_event_topic";

    /**
     * 消息可撤回时间 单位秒
     */
    public static final Integer IM_MESSAGE_WITH_DRAWN_SEC = 120;
}