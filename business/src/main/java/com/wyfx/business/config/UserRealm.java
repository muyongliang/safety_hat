package com.wyfx.business.config;

import com.alibaba.fastjson.JSON;
import com.wyfx.business.controller.ws.pojo.BaseCommand;
import com.wyfx.business.controller.ws.pojo.WsConstant;
import com.wyfx.business.entity.BusinessInfo;
import com.wyfx.business.entity.BusinessUser;
import com.wyfx.business.entity.UserRole;
import com.wyfx.business.entity.vo.RoleMenuVo;
import com.wyfx.business.service.ClientAccountService;
import com.wyfx.business.service.common.IBusinessInfoService;
import com.wyfx.business.service.shiro.BusinessUserService;
import com.wyfx.business.service.shiro.MenuManagerService;
import com.wyfx.business.service.shiro.RoleService;
import com.wyfx.business.service.shiro.UserRoleService;
import com.wyfx.business.utils.ConstantList;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author johnson liu
 * @date 2019/11/2
 * @description 自定义shiro的安全数据源, 包含认证和授权两大模块, Realm可以存在多个
 */
public class UserRealm extends AuthorizingRealm {
    private static final Logger logger = LoggerFactory.getLogger(UserRealm.class);

    @Autowired
    private BusinessUserService businessUserService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private IBusinessInfoService iBusinessInfoService;
    @Autowired
    private MenuManagerService meunManagerService;
    @Autowired
    private ClientAccountService clientAccountService;
    /*@Autowired
    private RedisSessionDAO redisSessionDAO;*/
    @Value("#{'admin'}")
    private String adminAccount;


    /**
     * 用户认证(从数据库等中校验用户是否合法)
     *
     * @param authenticationToken AuthenticationToken身份认证 token
     * @return AuthenticationInfo 身份认证信息
     * @throws AuthenticationException 认证相关异常
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        logger.debug("******************************执行认证过程doGetAuthenticationInfo****************************");
        //获取用户输入的用户名和密码
        String name = (String) authenticationToken.getPrincipal();//实体信息(Principals)
        String pwd = new String((char[]) authenticationToken.getCredentials());//凭据信息(Credentials)

        Integer source = null;
        String imei = null;

        if (authenticationToken instanceof MyUsernamePasswordToken) {
            source = ((MyUsernamePasswordToken) authenticationToken).getSource();
        }

        BusinessUser user = businessUserService.findByUserName(name);
        if (user == null) {
            throw new UnknownAccountException("用户名或密码错误,请重试！");
        }
        int userType = user.getUserType();
        //处理之前登录账号的session和webSocket连接
        if (source != 666) {
            if (userType != 0) {
                Map<String, javax.websocket.Session> sessionMap = ConstantList.sessionMap.get(name);
                if (sessionMap != null) {
                    String from = null;
                    switch (source) {
                        case 0:
                            from = "web";
                            break;
                        case 1:
                            from = "android";
                            break;
                        default:
                            break;
                    }
                    javax.websocket.Session session = sessionMap.get(from);
                    if (session != null) {
                        String message = JSON.toJSONString(new BaseCommand(WsConstant.logout.name(), "", null));
                        try {
                            session.getBasicRemote().sendText(message);//发送退出命令
                            logger.info("发送了退出命令:" + name);
                        } catch (Exception e) {
                            logger.error("登录时退出之前账号失败", e);
                        }
                    }
                }
            }
            DefaultWebSecurityManager securityManager = (DefaultWebSecurityManager) SecurityUtils.getSecurityManager();
            DefaultWebSessionManager sessionManager = (DefaultWebSessionManager) securityManager.getSessionManager();
            Collection<Session> sessions = sessionManager.getSessionDAO().getActiveSessions();//获取当前已登录的用户session列表
            String obj = null;
            StringBuffer stringBuffer = new StringBuffer();
            for (Session session : sessions) {
                //清除该用户以前登录时保存的session
                stringBuffer.setLength(0);
                stringBuffer.append("userName='");
                stringBuffer.append(name);
                stringBuffer.append("'");
                obj = String.valueOf(session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY));
                if (obj.contains(stringBuffer.toString()) && session.getAttribute("source").equals(source.toString())) {
                    sessionManager.getSessionDAO().delete(session);
                    logger.info("移除先前登录的账号:" + name);
                }
            }
        }
        BusinessInfo businessInfo = iBusinessInfoService.findByToken(user.getToken());
        if (businessInfo.getStatus() == 1) {
            //暂停合作
            throw new UnknownAccountException("已暂停合作,无法登录");
        }

        if (source != null && ((source == 0 && userType == 2) || (source == 1 && userType != 1) || (source == 2 && userType != 2))) {
            //终端账号不能在web上登录,终端app不能登录非终端用户，调度员app不能登录非调度员用户
            throw new UnknownAccountException("账号不能跨平台登录");
        }
        /*if(source==2 && userType==2){
            imei=((MyUsernamePasswordToken)authenticationToken).getImei();
            String imeiOfDB=clientAccountService.findIMEIByBid(user.getBid());
            if(imeiOfDB==null || imei==null || !imei.equals(imeiOfDB)){
                throw new IncorrectCredentialsException("不是本设备的IMEI,不允许登录");
            }
        }*/

        //todo 免密登录 source不等于666时才做密码匹配
        /*if(source!=null && source!=666){
            if (!StringUtils.equals(pwd, user.getPassword().trim())){
                throw new IncorrectCredentialsException("用户名或密码错误,请重试!");
            }
        }*/

        if (user.getStatus() == BusinessUser.STATUS_LOCK) {
            throw new LockedAccountException("账号已被锁定,请联系管理员！");
        }

        logger.debug("********************************执行认证过程doGetAuthenticationInfo:" + user.getUserName() + "通过认证!");
        return new SimpleAuthenticationInfo(user, pwd, getName());
    }

    /**
     * 授权模块:为当前登录的用户授予角色和权限
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        logger.debug("******************************执行授权模块过程doGetAuthorizationInfo****************************");
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        //如果授权部分没有传入User对象，这里只能取到userName
        //也就是SimpleAuthenticationInfo构造的时候第一个参数传递需要AdminUser对象
        BusinessUser user = (BusinessUser) principalCollection.getPrimaryPrincipal();

        //获取用户角色集
        List<UserRole> userRoleList = userRoleService.findByUserId(user.getBid().intValue());
        Set<String> roleSet = new HashSet(userRoleList);
        System.out.println("角色集:" + roleSet);
        authorizationInfo.setRoles(roleSet);

        //获取用户权限集
        List<RoleMenuVo> permissionList = meunManagerService.findUserPermissions(userRoleList);
        Set<String> permissionSet = permissionList.stream().map(RoleMenuVo::getRemark).collect(Collectors.toSet());
        authorizationInfo.setStringPermissions(permissionSet);
        logger.debug("********************************执行授权模块过程doGetAuthorizationInfo:" + user.getUserName() + "通过授权!");
        return authorizationInfo;
    }

    /**
     * 清除当前用户权限缓存
     * 使用方法：在需要清除用户权限的地方注入 ShiroRealm,
     * 然后调用其 clearCache方法。
     */
    public void clearCache() {
        PrincipalCollection principals = SecurityUtils.getSubject().getPrincipals();
        /*super.clearCachedAuthorizationInfo(principals);*/
        super.clearCache(principals);
    }

    /**
     * 为超级管理员添加所有权限
     *
     * @param principals
     * @param permission
     * @return
     */
    @Override
    public boolean isPermitted(PrincipalCollection principals, String permission) {
        String userName = principals.getPrimaryPrincipal().toString();
        return adminAccount.equals(userName) || super.isPermitted(principals, permission);
    }

    /**
     * 为超级管理员添加所有角色
     *
     * @param principals
     * @param roleIdentifier
     * @return
     */
    @Override
    public boolean hasRole(PrincipalCollection principals, String roleIdentifier) {
        String userName = principals.getPrimaryPrincipal().toString();
        return adminAccount.equals(userName) || super.hasRole(principals, roleIdentifier);
    }
}
