package com.wyfx.business.config;

import com.wyfx.business.utils.FilePathUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

/**
 * @Author johnliu
 * @create 2019/1/9 15:23
 * @Description 将应用外部文件资源映射成程序静态资源
 * WebMvcConfigurerAdapter已过期，所以使用WebMvcConfigurationSupport替代
 **/
@Configuration
public class FilePathConfig implements WebMvcConfigurer {
    private static final Logger logger = LoggerFactory.getLogger(FilePathConfig.class);

    @Value("${file.staticAccessPath}")
    private String staticAccessPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String parent = FilePathUtil.getExcuteJarPath();
        String gitPath = parent + File.separator + "safety-hat" + File.separator;
        System.out.println("本地路径映射:" + gitPath);
//        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler(staticAccessPath).addResourceLocations("file:" + gitPath);

    }
}
