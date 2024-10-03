package cn.ether.im.push;

/**
 * * @Author: Martin
 * * @Date    2024/9/14 20:18
 * * @Description
 **/
public interface ImPushServer {

    boolean ready();

    void start();

    void shutdown();


}
