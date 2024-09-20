package cn.ether.im.message;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * * @Author: Martin
 * * @Date    2024/9/15 12:28
 * * @Description
 **/
@SpringBootApplication(scanBasePackages = "cn.ether.im")
public class EtherImMessageApplication {

    public static void main(String[] args) {
        System.setProperty("rocketmq.client.logUseSlf4j", "false");
        SpringApplication.run(EtherImMessageApplication.class);
    }

}
