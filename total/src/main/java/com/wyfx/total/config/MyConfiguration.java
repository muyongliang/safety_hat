package com.wyfx.total.config;

import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @ClassName: MyConfiguration
 * @Description: 跨越配置
 * @author： 张古良
 * @date 2019-11-2
 */
//@Configuration
@Deprecated
public class MyConfiguration {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        System.out.println("MyConfiguration.corsConfigurer()=" + 123456);
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**");
            }
        };
    }
}