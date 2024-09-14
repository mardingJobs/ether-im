package cn.ether.im.push;

import cn.ether.im.common.constants.ImConstants;
import cn.ether.im.common.enums.ImTerminalType;
import cn.ether.im.common.model.ImUser;
import cn.ether.im.common.util.JwtUtils;
import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * * @Author: Martin
 * * @Date    2024/9/14 22:42
 * * @Description
 **/
public class UserMockTest {

    private String secret = "marding";

    @Test
    public void test() {
        ImUser imUser = new ImUser();
        imUser.setUserId("123");
        imUser.setTerminalType(ImTerminalType.WEB);
        imUser.setGroup("user");

        String sign = JwtUtils.sign(imUser.getUserId(), JSON.toJSONString(imUser), ImConstants.ONLINE_TIMEOUT_SECONDS, secret);
        System.out.println(sign);
    }

}
