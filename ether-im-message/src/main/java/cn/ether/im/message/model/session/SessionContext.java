package cn.ether.im.message.model.session;

import cn.ether.im.common.enums.ImExceptionCode;
import cn.ether.im.common.exception.ImException;
import cn.ether.im.message.model.constants.ImMessageConstants;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class SessionContext {

    public static ImUserSession getSession() {
        // 从请求上下文里获取Request对象
        ServletRequestAttributes requestAttributes = ServletRequestAttributes.class.
                cast(RequestContextHolder.getRequestAttributes());
        HttpServletRequest request = requestAttributes.getRequest();
        Object object = request.getAttribute(ImMessageConstants.SESSION);
        if (object == null) {
            throw new ImException(ImExceptionCode.UN_LOGGED);
        }
        return (ImUserSession) object;
    }
}