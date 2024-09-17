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


public enum ImMessageStatus {

    UN_SEND("未发送"),
    SENT("已发送"),
    PUSHED("已推送"),
    WITH_DRAWN("已撤回"),
    REACHED("已触达"),
    READ("已读");

    private final String desc;

    ImMessageStatus(String desc) {
        this.desc = desc;
    }


}
