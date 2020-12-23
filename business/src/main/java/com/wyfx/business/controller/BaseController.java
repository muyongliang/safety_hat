package com.wyfx.business.controller;

import com.wyfx.business.config.exception.SafetyHatSessionException;
import com.wyfx.business.dao.BusinessUserMapper;
import com.wyfx.business.entity.BusinessUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author johnson liu
 * @date 2019/11/2
 * @description 从shiro中获取Session相关信息
 */
@Component
@Slf4j
public class BaseController {

    @Value("${remote.url}")
    protected String remoteUrl;
    @Autowired
    BusinessUserMapper businessUserMapper;


    public Subject getSubject() {
        return SecurityUtils.getSubject();
    }

    public BusinessUser getCurrentUser() {
        String principal = (String)getSubject().getPrincipal();
        log.info("获取当前用户principal:{}",principal);
        BusinessUser user = businessUserMapper.findByUserName(principal) ;
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
