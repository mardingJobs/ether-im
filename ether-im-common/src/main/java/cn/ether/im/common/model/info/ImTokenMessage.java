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
package cn.ether.im.common.model.info;

import cn.ether.im.common.enums.ImSysMessageType;
import cn.ether.im.common.model.info.sys.ImSysMessage;
import lombok.Data;

@Data
public class ImTokenMessage extends ImSysMessage {

    private String token;

    public ImTokenMessage() {
        this.setSystemMessageType(ImSysMessageType.TOKEN);
    }

    public ImTokenMessage(String token) {
        this.token = token;
        this.setSystemMessageType(ImSysMessageType.TOKEN);
    }
}
