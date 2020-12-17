package com.wyfx.total.config;

import com.alibaba.fastjson.JSON;
import com.wyfx.total.utile.RespBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class AllInterceptor implements HandlerInterceptor {


    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    private static final Logger logger = LoggerFactory.getLogger(AllInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest,
                             HttpServletResponse httpServletResponse, Object o) throws Exception {
        logger.info("preHandle");
        StringBuffer sb = new StringBuffer();
        sb.append(sdf.format(new Date()))
                .append(" [").append(Thread.currentThread().getId()).append("] ")
                .append(httpServletRequest.getMethod()).append(" ")
                .append(httpServletRequest.getRequestURL()).append(" ")
                .append(httpServletRequest.getQueryString());
        logger.info(sb.toString());

        //统一拦截未登录请求
        HttpSession session = httpServletRequest.getSession();
        Integer uid = (Integer) session.getAttribute("uid");

        if (null == uid) {
            logger.error("未登录,拦截");
            //未登录统一返回自定义响应对象RespBean给前端,并返回false
            RespBean respBean = new RespBean();//创建响应对象
            respBean.setCode(666);
            respBean.setData("未登录,拦截");
            myResponse(httpServletResponse, respBean);//统一响应给前端
            return false;
        } else {
            logger.info("已登录,放行");
            return true;
        }
    }


    /**
     * 自定义响应数据给前端
     *
     * @param response
     * @param codeMsg
     * @throws Exception
     */
    private void myResponse(HttpServletResponse response, RespBean codeMsg) {
        response.setContentType("application/json;charset=UTF-8");
        OutputStream out = null;
        try {
            out = response.getOutputStream();
            String str = JSON.toJSONString(codeMsg);
            out.write(str.getBytes(StandardCharsets.UTF_8));
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) {
        logger.info("postHandle");
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        logger.info("afterCompletion");
    }
}

