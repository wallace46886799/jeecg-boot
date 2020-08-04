package io.github.logger.controller.utils;

import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import io.github.logger.controller.bean.RequestContext;

public class RequestUtil {

    public static RequestContext getRequestContext() {
        HttpServletRequest request = getCurrentHttpRequest();

        return new RequestContext()
                .add("url", getRequestUrl(request))
                .add("username", getRequestUserName());
    }

    @Nullable
    private static String getRequestUrl(@Nullable HttpServletRequest request) {
        return request == null ? null : request.getRequestURL().toString();
    }


    @Nullable
    private static String getRequestUserName() {
        return "dreamlabs";
    }

    @Nullable
    private static HttpServletRequest getCurrentHttpRequest() {
        HttpServletRequest request = null;
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null && requestAttributes instanceof ServletRequestAttributes) {
            request = ((ServletRequestAttributes)requestAttributes).getRequest();
        }
        return request;
    }


}
