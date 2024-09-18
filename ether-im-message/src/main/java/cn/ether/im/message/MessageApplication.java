package cn.ether.im.message;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * * @Author: Martin
 * * @Date    2024/9/15 12:28
 * * @Description
 **/
@SpringBootApplication(scanBasePackages = "cn.ether.im")
public class MessageApplication {

    public static void main(String[] args) {
        System.setProperty("rocketmq.client.logUseSlf4j", "true");
        SpringApplication.run(MessageApplication.class);
    }

}
