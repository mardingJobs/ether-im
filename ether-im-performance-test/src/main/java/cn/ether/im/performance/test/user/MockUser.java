package cn.ether.im.performance.test.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.List;

/**
 * * @Author: Martin(微信：martin-jobs)
 * * @Date    2024/9/27 13:04
 * * @Description
 * * @Github <a href="https://github.com/mardingJobs">Github链接</a>
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MockUser {

    private String userId;

    private String terminalType;


    public static List<MockUser> createUsers(int count) {
        List<MockUser> users = new LinkedList<>();
        for (int i = 0; i < count; i++) {
            MockUser pcUser = new MockUser("user-" + i, "WEB");
            MockUser appUser = new MockUser("user-" + i, "APP");
            users.add(pcUser);
            users.add(appUser);
        }
        return users;
    }

}
