package com.wyfx.business.config;

import org.apache.shiro.authz.AuthorizationInfo;

/**
 * @author johnson liu
 * @date 2019/11/5
 * @description 自定义shiro的安全数据源, 包含认证和授权两大模块, Realm可以存在多个
 */
public class ShiroHelper extends UserRealm {

    /**
     * 获取当前用户的角色和权限集合
     *
     * @return AuthorizationInfo
     */
    public AuthorizationInfo getCurrentuserAuthorizationInfo() {
        return super.doGetAuthorizationInfo(null);
    }
}
