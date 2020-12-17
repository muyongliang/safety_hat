package com.wyfx.business.config;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author johnson liu
 * @date 2019/11/2
 * @description 登录拦截认证
 */
@Configuration
public class ShiroConfig {

    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private int port;
    @Value("${spring.redis.timeout}")
    private String timeout;
    @Value("${spring.redis.password}")
    private String password;

    /**
     * Shiro生命周期处理器
     */
    @Bean
    public static LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

        /*Map<String,Filter> filters= shiroFilterFactoryBean.getFilters();
        //将自定义的AuthenticationFilter注入shiroFilter中，注意是authc过滤器
        filters.put("authc", new CORSAuthenticationFilter());*/

        shiroFilterFactoryBean.setSecurityManager(securityManager);


        //自定义拦截器
        /*Map<String, Filter> filtersMap = new LinkedHashMap<String, Filter>();
        //限制同一帐号同时在线的个数。
        filtersMap.put("kickout", kickoutSessionControlFilter());
        shiroFilterFactoryBean.setFilters(filtersMap);
*/
        //拦截器.
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
        // 配置不会被拦截的链接 顺序判断
        filterChainDefinitionMap.put("/admin/**", "anon");
        //filterChainDefinitionMap.put("/talkBack/**","roles['调度员']");
        //配置退出 过滤器,其中的具体的退出代码Shiro已经替我们实现了
        /*filterChainDefinitionMap.put("/logout", "logout");*/
        //过滤链定义，从上向下顺序执行，一般将放在最为下边这是一个坑呢，一不小心代码就不好使了;
        //authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问 authc
        filterChainDefinitionMap.put("/video/**", "anon");
        filterChainDefinitionMap.put("/favicon.ico", "anon");
        filterChainDefinitionMap.put("/broadcast/**", "anon");
        filterChainDefinitionMap.put("/safety-hat/**", "anon");
        filterChainDefinitionMap.put("/js/**", "anon");
        filterChainDefinitionMap.put("/css/**", "anon");
        filterChainDefinitionMap.put("/img/**", "anon");
        filterChainDefinitionMap.put("/0", "anon");
        filterChainDefinitionMap.put("/*.html", "anon");
        filterChainDefinitionMap.put("/fonts/**", "anon");
        filterChainDefinitionMap.put("/device/handle*", "anon");
        filterChainDefinitionMap.put("/file/handle*", "anon");
        filterChainDefinitionMap.put("/system/handleSelectLogByCondition", "authc");
        filterChainDefinitionMap.put("/system/handleExportLog", "authc");
        filterChainDefinitionMap.put("/system/handleUpdateManuallyApp", "authc");
        filterChainDefinitionMap.put("/system/handleUpdateAppVersionSet", "authc");
        filterChainDefinitionMap.put("/system/handle*", "anon");
        filterChainDefinitionMap.put("/system/updateBusinessOfSetting", "anon");
        filterChainDefinitionMap.put("/client/handle*", "anon");
        filterChainDefinitionMap.put("/client/selectBusinessStatistics", "anon");
        filterChainDefinitionMap.put("/dispatcher/handle*", "anon");
        filterChainDefinitionMap.put("/dispatcher/getDispatcherAppUpdateData", "anon");
        filterChainDefinitionMap.put("/messagesCenter/handle*", "anon");
        filterChainDefinitionMap.put("/user/handle*", "anon");
        filterChainDefinitionMap.put("/alarmRange/receiveAlarmInfo", "anon");
        filterChainDefinitionMap.put("/client/getClientAppUpdateData", "anon");
        filterChainDefinitionMap.put("/webjars/**", "anon");
        filterChainDefinitionMap.put("/swagger*", "anon");
        filterChainDefinitionMap.put("/csrf*", "anon");
        filterChainDefinitionMap.put("/docs/**", "anon");
        filterChainDefinitionMap.put("/druid/**", "anon");
        filterChainDefinitionMap.put("/**", "authc");

        // 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
        shiroFilterFactoryBean.setLoginUrl("/admin/toIndex");
        // 登录成功后要跳转的链接
        //shiroFilterFactoryBean.setSuccessUrl("/home");

        //未授权界面;
        //shiroFilterFactoryBean.setUnauthorizedUrl("/403");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

        return shiroFilterFactoryBean;
    }

    /**
     * 凭证匹配器
     * （由于我们的密码校验交给Shiro的SimpleAuthenticationInfo进行处理了 ）
     *
     * @returnstoredCredentialsHexEncoded
     */
    /*@Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("md5");//散列算法:这里使用MD5算法;
        hashedCredentialsMatcher.setHashIterations(2);//散列的次数，比如散列两次，相当于 md5(md5(""));
        //hashedCredentialsMatcher.setStoredCredentialsHexEncoded(true);
        return hashedCredentialsMatcher;
    }*/
    @Bean
    public UserRealm userRealm() {
        UserRealm userRealm = new UserRealm();
        userRealm.setCredentialsMatcher(new CredentialMatcher());

        userRealm.setCachingEnabled(true);
        //启用身份验证缓存，即缓存AuthenticationInfo信息，默认false
        userRealm.setAuthenticationCachingEnabled(false);
        //缓存AuthenticationInfo信息的缓存名称
        userRealm.setAuthenticationCacheName("authenticationCache");
        //启用授权缓存，即缓存AuthorizationInfo信息，默认false
        userRealm.setAuthorizationCachingEnabled(true);
        //缓存AuthorizationInfo信息的缓存名称
        userRealm.setAuthorizationCacheName("authorizationCache");

        return userRealm;
    }

    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 自定义缓存实现 使用redis
//        securityManager.setCacheManager(cacheManager());
        securityManager.setSessionManager(sessionManager());
        //自定义realm
        securityManager.setRealm(userRealm());
        return securityManager;
    }

    /**
     * 开启自动代码,设置为true即可
     *
     * @return
     */
    @Bean
    @DependsOn({"getLifecycleBeanPostProcessor"})
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();
        /*creator.setUsePrefix(true);*/
        creator.setProxyTargetClass(true);
        return creator;
    }

    /**
     * Session Manager
     * 使用的是shiro-redis开源插件
     */
    @Bean
    public DefaultWebSessionManager sessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        //单位是毫秒,设置session的有效时间是30分钟
        sessionManager.setSessionIdUrlRewritingEnabled(false);
        sessionManager.setGlobalSessionTimeout(30 * 60 * 1000);
        sessionManager.setSessionValidationSchedulerEnabled(true);
        //定时查询所有session是否过期的时间
        sessionManager.setSessionValidationInterval(30 * 60 * 1000);
        sessionManager.setDeleteInvalidSessions(true);
        sessionManager.setSessionIdCookie(cookie());
        sessionManager.setSessionIdCookieEnabled(true);

//        sessionManager.setSessionDAO(redisSessionDAO());
        return sessionManager;
    }

    @Bean
    public SimpleCookie cookie() {
        //  cookie的name,对应的默认是 JSESSIONID；QIUSESSIONID
        SimpleCookie cookie = new SimpleCookie("BUSINESS_SESSIONID");
        cookie.setHttpOnly(false);
        cookie.setMaxAge(-1);
        /*cookie.setPath("/");    */    //  path为 / 用于多个系统共享JSESSIONID
        return cookie;
    }

    /**
     * 解决：无权限页面不跳转 shiroFilterFactoryBean.setUnauthorizedUrl("/unauthorized") 无效
     * shiro的源代码ShiroFilterFactoryBean.Java定义的filter必须满足filter instanceof AuthorizationFilter，
     * 只有perms，roles，ssl，rest，port才是属于AuthorizationFilter，而anon，authcBasic，auchc，user是AuthenticationFilter，
     * 所以unauthorizedUrl设置后页面不跳转 Shiro注解模式下，登录失败与没有权限都是通过抛出异常。
     * 并且默认并没有去处理或者捕获这些异常。在SpringMVC下需要配置捕获相应异常来通知用户信息
     *
     * @return
     */
    @Bean(name = "simpleMappingExceptionResolver")
    public SimpleMappingExceptionResolver createSimpleMappingExceptionResolver() {
        SimpleMappingExceptionResolver r = new SimpleMappingExceptionResolver();
        Properties mappings = new Properties();
        //mappings.setProperty("DatabaseException", "databaseError");//数据库异常处理
        mappings.setProperty("UnauthorizedException", "/admin/403");
        r.setExceptionMappings(mappings);  // None by default
        r.setExceptionAttribute("exception");     // Default is "exception"
        return r;
    }

    /**
     * cacheManager 缓存 redis实现
     * 使用的是shiro-redis开源插件
     *
     * @return
     */
    public RedisCacheManager cacheManager() {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        //redis默认是使用id作为key存储session信息,在主键不一致的情况下,需要重新设置
        redisCacheManager.setPrincipalIdFieldName("bid");
        return redisCacheManager;
    }

    /**
     * RedisSessionDAO shiro sessionDao层的实现 通过redis
     * 使用的是shiro-redis开源插件
     */
    @Bean
    public RedisSessionDAO redisSessionDAO() {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(redisManager());
        return redisSessionDAO;
    }

    /**
     * 配置shiro redisManager
     * 使用的是shiro-redis开源插件
     *
     * @return
     */
    public RedisManager redisManager() {
        RedisManager redisManager = new RedisManager();
        String hostStr = host + ":" + port;
        redisManager.setHost(hostStr);
        if (!password.equals("") && password != null) {
            redisManager.setPassword(password);
        }
        int time = Integer.valueOf(timeout.substring(0, timeout.indexOf("ms")));
        redisManager.setTimeout(time);
        return redisManager;
    }

    /**
     * 开启shiro aop注解支持.
     * 使用代理方式;所以需要开启代码支持;
     *
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

}
