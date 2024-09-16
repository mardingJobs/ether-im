package cn.ether.im.common.model.user;

import cn.ether.im.common.enums.ImTerminalType;
import lombok.Data;

/**
 * todo 改为继承
 * * @Author: Martin
 * * @Date    2024/9/15 21:31
 * * @Description
 **/
@Data
public class ImUserTerminal extends ImUser {


    /**
     * 终端类型
     */
    private ImTerminalType terminalType;


    public ImUserTerminal(String userId, ImTerminalType terminalType, String group) {
        this.setUserId(userId);
        this.setGroup(group);
        this.terminalType = terminalType;
    }

    public ImUserTerminal(ImUser user, ImTerminalType terminalType) {
        this.setUserId(user.getUserId());
        this.setGroup(user.getGroup());
        this.terminalType = terminalType;
    }


}
