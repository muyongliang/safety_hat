package com.wyfx.business.config;

import org.springframework.context.annotation.Configuration;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author johnson liu
 * @date 2019/11/12
 * @description 跨域访问支持:前后端分离项目中，由于跨域，会导致复杂请求，即会发送preflighted request，这样会导致在GET／POST等请求之前会
 * 先发一个OPTIONS请求，但OPTIONS请求并不带shiro的'Authorization'字段（shiro的Session），即OPTIONS请求不能通过shiro验证，会返回未认证的信息
 */
@Configuration
@WebFilter(filterName = "UrlFilter", urlPatterns = "/*")
public class UrlFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        Cookie[] cookies = request.getCookies();
        // 允许哪些Origin发起跨域请求
        String orgin = request.getHeader("Origin");
        if (orgin == null) {
            orgin = request.getHeader("Referer");
        }
        response.setHeader("Access-Control-Allow-Origin", orgin);
        // 允许请求的方法
        response.setHeader("Access-Control-Allow-Methods", "POST,GET,OPTIONS,DELETE,PUT");
        //多少秒内,不需要再发送预检验请求，可以缓存该结果
        response.setHeader("Access-Control-Max-Age", "3600");
        // 表明它允许跨域请求包含xxx头
        response.setHeader("Access-Control-Allow-Headers", "x-auth-token,Origin,Access-Token,X-Requested-With,Content-Type, Accept");
        //是否允许浏览器携带用户身份信息（cookie）
        response.setHeader("Access-Control-Allow-Credentials", "true");
        //response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        //prefight请求
        if (request.getMethod().equals("OPTIONS")) {
            response.setStatus(200);
            return;
        }
        String uri = request.getRequestURI();
        String contentPath = request.getContextPath();
        String actionParam = request.getParameter("action");
        String idParam = request.getParameter("id");
        //http://localhost:8080?action=cs02&id=666
        if (uri.equals("/") && idParam == null && actionParam == null) {
            response.sendRedirect("/index.html");
            return;
        }
        if (uri.equals("/") && idParam.equals("666") && actionParam != null) {
            response.sendRedirect("/index.html?action=" + actionParam + "&id=666");
            return;
        }

        filterChain.doFilter(servletRequest, response);
    }

    @Override
    public void destroy() {

    }
}
