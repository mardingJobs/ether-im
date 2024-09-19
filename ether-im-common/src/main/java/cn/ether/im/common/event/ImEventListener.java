package cn.ether.im.common.event;


import cn.ether.im.common.enums.ImMessageEventType;
import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface ImEventListener {

    ImMessageEventType[] listenEventTypes() default {};

    @AliasFor("listenEventTypes")
    ImMessageEventType[] types() default {};
}
