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
package cn.ether.im.common.model.user;

import cn.ether.im.common.constants.ImConstants;
import com.google.common.base.Objects;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * IM 用户信息
 */
@Data
@NoArgsConstructor
public class ImUser {

    /**
     * 用户id
     */
    private String userId;

    /**
     * 用户分组
     */
    private String group = ImConstants.DEFAULT_GROUP_NAME;


    public ImUser(String userId) {
        this.userId = userId;
    }

    public ImUser(String userId, String group) {
        this.userId = userId;
        this.group = group;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ImUser)) return false;
        ImUser user = (ImUser) o;
        return Objects.equal(userId, user.userId) && Objects.equal(group, user.group);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(userId, group);
    }
}
