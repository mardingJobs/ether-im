package cn.ether.im.common.model;

import cn.ether.im.common.constants.ImConstants;
import cn.ether.im.common.enums.ImTerminalType;
import lombok.Data;

/**
 * * @Author: Martin
 * * @Date    2024/9/15 21:31
 * * @Description
 **/
@Data
public class ImUserTerminal {

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
