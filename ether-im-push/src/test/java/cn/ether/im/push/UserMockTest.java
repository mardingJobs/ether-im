package cn.ether.im.push;

import cn.ether.im.common.enums.ImTerminalType;
import cn.ether.im.common.model.user.ImUser;
import cn.ether.im.common.model.user.ImUserTerminal;
import cn.ether.im.common.util.JwtUtils;
import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;

/**
 * * @Author: Martin
 * * @Date    2024/9/14 22:42
 * * @Description
 **/
public class UserMockTest {

    private String secret = "marding";

    @Test
    public void sign() {
        ImUser user = new ImUser("123", "customer");
        ImUserTerminal imUserTerminal = new ImUserTerminal(user, ImTerminalType.WEB);
        String sign = JwtUtils.sign(user.getUserId(), JSON.toJSONString(imUserTerminal), 100000, secret);
        System.out.println(sign);
    }

}
