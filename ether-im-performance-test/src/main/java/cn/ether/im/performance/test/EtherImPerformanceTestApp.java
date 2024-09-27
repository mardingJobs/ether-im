package cn.ether.im.performance.test;

import cn.ether.im.performance.test.connection.WebSocketUserConnections;

import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;

/**
 * * @Author: Martin(微信：martin-jobs)
 * * @Date    2024/9/27 12:46
 * * @Description
 * * @Github <a href="https://github.com/mardingJobs">Github链接</a>
 **/
public class EtherImPerformanceTestApp {

    public static void main(String[] args) throws URISyntaxException, InterruptedException {
        int length = args.length;
        WebSocketUserConnections webSocketUserConnections = new WebSocketUserConnections();
        if (length == 1) {
            webSocketUserConnections.setUserCount(Integer.parseInt(args[0]));
        } else if (length == 2) {
            webSocketUserConnections.setUserCount(Integer.parseInt(args[0]));
            webSocketUserConnections.setUrl(args[1]);
        }
        webSocketUserConnections.connect();

        new CountDownLatch(1).await();
    }

}
