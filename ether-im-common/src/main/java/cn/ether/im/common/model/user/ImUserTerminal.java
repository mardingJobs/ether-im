package cn.ether.im.common.model.user;

import cn.ether.im.common.enums.ImTerminalType;
import lombok.Data;

/**
 * * @Author: Martin
 * * @Date    2024/9/15 21:31
 * * @Description
 **/
@Data
public class ImUserTerminal {


    private ImUser user;

    /**
     * 终端类型
     */
    private ImTerminalType terminalType;


    public ImUserTerminal(ImUser imUser, ImTerminalType terminalType) {
        this.user = imUser;
        this.terminalType = terminalType;
    }


}
