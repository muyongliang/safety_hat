package com.wyfx.total.aspect;

import java.lang.annotation.*;

/**
 * 控制器切面注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface ControllerLog {
    String description() default "";//描述

    String actionType() default "";//操作的类型，1、添加 2、修改 3、删除 4、查询
}
