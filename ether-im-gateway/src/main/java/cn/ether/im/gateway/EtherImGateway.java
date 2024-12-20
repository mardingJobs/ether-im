package cn.ether.im.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * * @Author: Martin(微信：martin-jobs)
 * * @Date    2024/9/20 13:41
 * * @Description
 * * @Github <a href="https://github.com/mardingJobs">Github链接</a>
 **/
@EnableDiscoveryClient
@SpringBootApplication
public class EtherImGateway {

    public static void main(String[] args) {
        SpringApplication.run(EtherImGateway.class, args);
    }

}
