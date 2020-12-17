package com.wyfx.business.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Author: liuxingyu
 * DATE: 2017-07-03.10:36
 * description:用户存储，获取,删除  t
 * version:
 */
public class WyfxContext {
    /**
     * 用户管理
     */
    public static String getUsername() {
        String loginUser = (String) getSession().getAttribute("username");
        return loginUser;
    }

    public static void saveUsername(String user) {
        HttpSession session = getSession();
        session.setAttribute("username", user);

    }

    public static void delUsername() {
        getSession().invalidate();
    }

    public static void setUsername() {
        getSession().setAttribute("username", null);
    }

    /**
     * 语言类型
     */
    public static String getLangue(HttpServletRequest request) {
        String langue = (String) request.getSession().getAttribute("langue");
        return langue;
    }

    public static String getLangue() {
        String langue = (String) getSession().getAttribute("langue");
        return langue;
    }

    public static void saveLangue(String langue) {
        getSession().setAttribute("langue", langue);
    }

    public static HttpSession getSession() {
        HttpSession session = null;
        try {
            session = getRequest().getSession();

        } catch (Exception e) {
        }
        return session;
    }

    public static HttpServletRequest getRequest() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attrs.getRequest();
    }


    public static String getUsername(HttpServletRequest request) {
        return (String) request.getSession().getAttribute("username");
    }
}
