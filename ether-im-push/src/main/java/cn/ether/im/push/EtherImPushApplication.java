package cn.ether.im.push;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * * @Author: Martin
 * * @Date    2024/9/14 12:27
 * * @Description
 **/
@SpringBootApplication(scanBasePackages = "cn.ether.im")
public class EtherImPushApplication {


    public static void main(String[] args) {
        SpringApplication.run(EtherImPushApplication.class, args);
    }

}
