package cn.ether.im.push;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

/**
 * * @Author: Martin
 * * @Date    2024/9/14 12:27
 * * @Description
 **/
@EnableRetry
@SpringBootApplication(scanBasePackages = "cn.ether.im")
public class EtherImPushApplication {


    public static void main(String[] args) {
        SpringApplication.run(EtherImPushApplication.class, args);
    }

}
