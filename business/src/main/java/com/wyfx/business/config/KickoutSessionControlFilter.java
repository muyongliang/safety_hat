package com.wyfx.business.config;

import com.alibaba.fastjson.JSON;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author johnson liu
 * @date 2019/11/16
 * @description 限制并发人数登录, 并允许手机电脑同时在线
 */
public class KickoutSessionControlFilter extends AccessControlFilter {

    private String kickoutUrl; //踢出后到的地址
    /*private boolean kickoutAfter = false; //踢出之前登录的/之后登录的用户 默认踢出之前登录的用户
    private int maxSession = 1; //同一个帐号最大会话数 默认1*/

    private SessionManager sessionManager;

    private Cache<String, Map<String, Serializable>> cache;//缓存Session队列


    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        Subject subject = getSubject(servletRequest, servletResponse);
        if (!subject.isAuthenticated() && !subject.isRemembered()) {
            //如果没有登录，直接进行之后的流程
            return true;
        }
        Session session = subject.getSession();
        String userName = (String) subject.getPrincipal();
        Serializable sessionId = session.getId();

        //读取缓存   没有就存入
        Map<String, Serializable> sessionMap = cache.get(userName);

        //如果此用户没有session队列，也就是还没有登录过，缓存中没有
        //就new一个空队列，不然deque对象为空，会报空指针
        if (sessionMap == null) {
            sessionMap = new HashMap<String, Serializable>();
        }
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String userAgent = request.getHeader("user-agent");
        boolean isMobile = userAgent.toLowerCase().contains("android");
        Serializable cacheSession = (isMobile) ? sessionMap.get("mobile") : sessionMap.get("web");

        //如果队列Map里没有此sessionId，且用户没有被踢出；放入队列,如果有,则覆盖先前登录是用户session
        if ((cacheSession == null || !sessionId.equals(cacheSession)) && session.getAttribute("kickout") == null) {
            if (isMobile) {
                sessionMap.put("mobile", sessionId);//将sessionId存入
            } else {
                sessionMap.put("web", sessionId);//将sessionId存入
            }
            cache.put(userName, sessionMap);//将用户的sessionId缓存/或更新

            try {
                //获取被踢出的sessionId的session对象
                Session kickoutSession = sessionManager.getSession(new DefaultSessionKey(cacheSession));
                if (kickoutSession != null) {
                    //设置会话的kickout属性表示踢出了
                    kickoutSession.setAttribute("kickout", true);
                }
            } catch (Exception e) {//ignore exception
            }
        }

        //如果被踢出了，直接退出，重定向到踢出后的地址
        if (session.getAttribute("kickout") != null) {
            //会话被踢出了
            try {
                //退出登录
                subject.logout();
            } catch (Exception e) { //ignore
            }
            saveRequest(request);

            Map<String, String> resultMap = new HashMap<String, String>();
            //判断是不是Ajax请求
            if ("XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With"))) {
                resultMap.put("code", "300");
                resultMap.put("message", "您已经在其他地方登录，请重新登录！");
                //输出json串
                out(servletResponse, resultMap);
            } else {
                //重定向到登录页面
                WebUtils.issueRedirect(request, servletResponse, kickoutUrl);
            }
            return false;
        }
        return true;
    }

    private void out(ServletResponse hresponse, Map<String, String> resultMap) throws IOException {
        try {
            hresponse.setCharacterEncoding("UTF-8");
            PrintWriter out = hresponse.getWriter();
            out.println(JSON.toJSONString(resultMap));
            out.flush();
            out.close();
        } catch (Exception e) {
            System.out.println("KickoutSessionFilter.class 输出JSON异常，可以忽略。");
        }
    }

    public void setKickoutUrl(String kickoutUrl) {
        this.kickoutUrl = kickoutUrl;
    }


    public void setSessionManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    //设置Cache的key的前缀
    public void setCacheManager(CacheManager cacheManager) {
        this.cache = cacheManager.getCache("safetyHat_redis_cache");
    }
}
