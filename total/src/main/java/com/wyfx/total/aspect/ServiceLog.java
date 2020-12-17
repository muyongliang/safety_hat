package com.wyfx.total.aspect;

import java.lang.annotation.*;

/**
 * 业务层切面注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface ServiceLog {
    //定义成员
    String description() default "";//描述

}
