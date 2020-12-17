package com.wyfx.business.controller;

import com.wyfx.business.config.exception.SafetyHatSessionException;
import com.wyfx.business.entity.BusinessUser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author johnson liu
 * @date 2019/11/2
 * @description 从shiro中获取Session相关信息
 */
@Component
public class BaseController {

    @Value("${remote.url}")
    protected String remoteUrl;


    public Subject getSubject() {
        return SecurityUtils.getSubject();
    }

    public BusinessUser getCurrentUser() {
        BusinessUser user = (BusinessUser) getSubject().getPrincipal();
        if (user == null) {
            throw new SafetyHatSessionException("未登录/会话已过期,请重新登录");
        }
        return user;
    }

    protected Session getSession() {
        Session session = getSubject().getSession();
        System.out.println("当前session的sessionId值:" + session.getId());
        return getSubject().getSession();
    }

    protected void login(AuthenticationToken token) {
        getSubject().login(token);
    }

}
