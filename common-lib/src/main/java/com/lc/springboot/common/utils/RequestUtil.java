package com.lc.springboot.common.utils;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * http请求封装类
 *
 * @author liangc
 * @version V1.0
 * @date 2020/9/17
 **/
public class RequestUtil {

    /**
     * 获取http请求对象
     *
     * @return
     */
    public static HttpServletRequest getHttpRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            return request;
        }
        return null;
    }

    /**
     * 获取http响应对象
     *
     * @return
     */
    public static HttpServletResponse getHttpResponse() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            HttpServletResponse response = ((ServletRequestAttributes) requestAttributes).getResponse();
            return response;
        }
        return null;
    }
}
