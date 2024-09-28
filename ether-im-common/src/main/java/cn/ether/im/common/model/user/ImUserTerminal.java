package cn.ether.im.common.model.user;

import cn.ether.im.common.enums.ImTerminalType;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * todo 改为继承
 * * @Author: Martin
 * * @Date    2024/9/15 21:31
 * * @Description
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class ImUserTerminal extends ImUser {


    /**
     * 终端类型
     */
    private ImTerminalType terminalType;

    public ImUserTerminal() {
    }

    public ImUserTerminal(String userId, ImTerminalType terminalType) {
        this.setUserId(userId);
        this.terminalType = terminalType;
    }

    public ImUserTerminal(ImUser user, ImTerminalType terminalType) {
        this.setUserId(user.getUserId());
        this.terminalType = terminalType;
    }

    public ImUser cloneUser() {
        return new ImUser(this.getUserId());
    }


    @Override
    public String toString() {
        return this.getUserId() + "@" + this.terminalType;
    }
}
