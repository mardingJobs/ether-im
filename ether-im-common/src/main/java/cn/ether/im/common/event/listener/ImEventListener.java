package cn.ether.im.common.event.listener;


import cn.ether.im.common.model.info.message.event.ImMessageEventType;
import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface ImEventListener {

    /**
     * 如果为空，代表监听所有消息事件类型
     *
     * @return
     */
    ImMessageEventType[] listenEventTypes() default {};

    @AliasFor("listenEventTypes")
    ImMessageEventType[] types() default {};
}
