package com.wyfx.business.config.annotation;

import java.lang.annotation.*;

/**
 * @Author johnson liu
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AopLog {
    String describe() default "";

    String targetParamName() default "";

    OperationType operationType() default OperationType.All;

}
