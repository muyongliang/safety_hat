package com.wyfx.total;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//1、使用@mapper后，不需要在spring配置中设置扫描地址，通过mapper.xml里面的namespace属性对应相关的mapper类，spring将动态的生成Bean后注入到ServiceImpl中。
//2、@repository则需要在Spring中配置扫描包地址，然后生成dao层的bean，之后被注入到ServiceImpl中
//一句话：不使用此注解(@MapperScan)就需要在所有的mapper接口上面添加@mapper注解，否则使用此注解一次性搞定，在mapper接口上面使用@repository保证自动注入时不爆红色
@MapperScan("com.wyfx.total.mapper")
@EnableScheduling//启动任务调度2019-11-19
public class TotalApplication {

    public static void main(String[] args) {
        SpringApplication.run(TotalApplication.class, args);
    }

}
