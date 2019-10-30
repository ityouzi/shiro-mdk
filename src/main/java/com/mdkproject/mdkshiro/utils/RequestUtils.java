package com.mdkproject.mdkshiro.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @program: mdk-shiro->RequestUtils
 * @description:
 * @author: lizhen
 * @create: 2019-08-05 13:45
 **/
public class RequestUtils {
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes ra= (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return ra.getRequest();
    }
}
