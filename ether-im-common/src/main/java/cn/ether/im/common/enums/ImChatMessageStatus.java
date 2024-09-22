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
package cn.ether.im.common.enums;


public enum ImChatMessageStatus {

    /**
     * 消息初始化状态，刚保存到数据库的状态
     */
    INTI("初始化"),
    /**
     * 消息发送到MQ中，等待被推送
     */
    SENT("已发送"),
    /**
     * 消息发送到MQ失败
     */
    SENT_FAIL("发送失败"),
    /**
     * 消息被推送
     */
    PUSHED("已推送"),
    /**
     * 消息被发送者撤回
     */
    WITH_DRAWN("已撤回"),
    /**
     * 消息已触达接收者终端
     */
    REACHED("已触达"),
    /**
     * 接收者已读消息
     */
    READ("已读");

    private final String desc;

    ImChatMessageStatus(String desc) {
        this.desc = desc;
    }


}
