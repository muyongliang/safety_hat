package com.wyfx.total.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

/**
 * 需要重新配置静态资源访问问题
 * 过滤器配置 包括过滤路径和放行路径配置
 *
 * @author admin
 */
@Configuration
//@EnableWebMvc  //  此注解代表启用自定义的mvc配置来替换所有的springboot自带的配置 谨慎使用 2019-11-22
public class WebMvcConfig implements WebMvcConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(WebMvcConfig.class);
    //创建拦截白名单
    List<String> excludePath = new ArrayList<>();
    @Autowired
    private AllInterceptor interceptor;
    /**
     * 对外暴露路径
     */
    @Value("${file.staticAccessPath}")
    private String staticAccessPath;
    /**
     * 文件上传路径
     */
    @Value("${file.uploadFolder}")
    private String uploadFolder;
    /**
     * 许可证保存路径
     */
    @Value("${license.licensePath}")
    private String licensePath;

    @Override//配置映射路径
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        logger.info("配置资源的映射路径");
        // 配置静态资源下载路径 “/img/**”静态资源映射的路径,"classpath:\\static\\img\\"静态资源所在的路径
        registry.addResourceHandler("/css/**").addResourceLocations("classpath:\\static\\css\\");
        registry.addResourceHandler("/fonts/**").addResourceLocations("classpath:\\static\\fonts\\");
        registry.addResourceHandler("/img/**").addResourceLocations("classpath:\\static\\img\\");
        registry.addResourceHandler("/js/**").addResourceLocations("classpath:\\static\\js\\");
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:\\static\\");

        //swagger-ui
        registry.addResourceHandler("/swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");


        //将上传资源的保存文件夹uploadFolder/路径映射为upload/**
        registry.addResourceHandler(staticAccessPath).addResourceLocations("file:" + uploadFolder, "file:" + licensePath);

    }

    @Override//配置无需拦截的路径即白名单
    public void addInterceptors(InterceptorRegistry registry) {
        //只有登录和注册页面允许未登录访问
        //首页
        excludePath.add("/index.html");
        excludePath.add("/index");
        //swagger 路径
        excludePath.add("/swagger-ui.html");
        excludePath.add("/webjars/**");
        excludePath.add("/docs/**");
        //登录
        excludePath.add("/login/Login");
        //注册
        excludePath.add("/login/register");
        //获取验证码
        excludePath.add("/login/getCode");
        //总后台页面设置
        excludePath.add("/system/handleSelectPageSetting");
        //企业后台请求企业自定义数据
        excludePath.add("/enterprise/handleSelectBusinessManagerDiySetting");
        //企业后台查询设备型号是否存在
        excludePath.add("/system/handleSelectDeviceType");
        //访问调度员app数据
        excludePath.add("/appManager/getDispatcherAppMessages");
        //访问终端app数据
        excludePath.add("/appManager/getClientAppMessages");
        //下载app崩溃日志
        excludePath.add("/remote/handleExportBugLogsByBid");

        //查询自定义设置
        excludePath.add("/authorizationManagement/handleQueryTBusinessDiySetting");
        //验证企业合法性
        excludePath.add("/authorizationManagement/handleVerificationTBusiness");

        //获取ip 和mac地址
        excludePath.add("/license/getServerInfos");
        //生成许可证书
        excludePath.add("/license/generateLicense");

        //安装许可证
        excludePath.add("/verifyLicense/handleInstallLicense");
        //验证许可证
        excludePath.add("/verifyLicense/handleVerifyLicense");
        //获取证书的起始时间
        excludePath.add("/verifyLicense/handleGetLicenseInfo");
        //上传许可证
        excludePath.add("/verifyLicense/handleUploadLicense");


        //静态资源
        excludePath.add("/static/**");
        excludePath.add("/css/**");
        excludePath.add("/fonts/**");
        excludePath.add("/js/**");
        excludePath.add("/img/**");
        //上传文件
        excludePath.add("/upload/**");

        //测试控制器test下的所有请求
        excludePath.add("/test/**");

        System.err.println("白名单路径=" + excludePath);
        //拦截所有请求 排除白名单列表
        registry.addInterceptor(interceptor).addPathPatterns("/**").excludePathPatterns(excludePath);
    }

    /**
     * 添加跨域配置
     * create on 2019-12-28
     *
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //设置允许跨域的路径
        registry.addMapping("/**")
                //设置允许跨域请求的域名
                .allowedOrigins("*")
                //是否允许证书 不再默认开启
                .allowCredentials(true)
                //设置允许的方法
                .allowedMethods("*")
                //跨域允许时间
                .maxAge(3600);
    }
}
