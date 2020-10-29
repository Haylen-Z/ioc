package com.github.mrgrtt.ioc.annotation;

import com.github.mrgrtt.ioc.Scope;

import java.lang.annotation.*;

/**
 * bean注解，用于向容器注册一个bean
 * @author Haylen
 * @date 2020-10-22
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Bean {
    String name() default "";
    Scope scope() default Scope.SINGLETON;
}
