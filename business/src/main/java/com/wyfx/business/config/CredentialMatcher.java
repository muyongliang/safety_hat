package com.wyfx.business.config;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;

/**
 * @author johnson liu
 * @date 2019/11/7
 * @description shiro校验重写设定
 */
public class CredentialMatcher extends SimpleCredentialsMatcher {
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
//        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
//        String password = new String(usernamePasswordToken.getPassword());
//        String dbPassword = (String) info.getCredentials();
//        return this.equals(password, dbPassword);

        //  免密认证
        MyUsernamePasswordToken tk = (MyUsernamePasswordToken) token;
        if (tk != null && tk.getSource() == 666) {
            return true;
        }
        boolean matches = super.doCredentialsMatch(token, info);
        return matches;
    }

}
