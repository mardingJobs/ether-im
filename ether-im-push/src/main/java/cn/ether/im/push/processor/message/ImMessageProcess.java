package cn.ether.im.push.processor.message;

import cn.ether.im.common.model.info.message.ImMessage;


public interface ImMessageProcess {


    default void process(ImMessage message) {
    }


}
