package com.github.mrgrtt.ioc.annotation;

import java.lang.annotation.*;

/**
 * 字段依赖注入注解
 * @author Haylen
 * @date 2020-10-22
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Inherited
@Documented
public @interface Inject {
    String name() default "";
}
