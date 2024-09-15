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
package cn.ether.im.common.model;

import cn.ether.im.common.constants.ImConstants;
import cn.ether.im.common.enums.ImTerminalType;
import lombok.Data;

/**
 * IM 用户信息
 */
@Data
public class ImUser {

    /**
     * 用户id
     */
    private String userId;

    /**
     * 用户分组
     */
    private String group = ImConstants.DEFAULT_GROUP_NAME;

    /**
     * 终端类型
     */
    private ImTerminalType terminalType;

}
