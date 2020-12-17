package com.wyfx.business.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author johnson liu
 * @date 2019/11/2
 * @description Session操作
 */
public class HttpRequestUtil {
    /**
     * 获取 HttpServletRequest
     *
     * @return
     */
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attrs.getRequest();
    }

    /**
     * 获取HttpSession
     *
     * @return HttpSession
     */
    public static HttpSession getSession() {
        HttpSession session = null;
        try {
            session = getRequest().getSession();
        } catch (Exception e) {
        }
        return session;
    }

    /**
     * 从Session中获取保存的用户名
     *
     * @return
     */
    public static String getUsername() {
        String loginUser = (String) getSession().getAttribute("username");
        return loginUser;
    }

    /**
     * 保存用户名到Session中
     *
     * @param user
     */
    public static void saveUsername(String user) {
        HttpSession session = getSession();
        session.setAttribute("username", user);
    }

}
