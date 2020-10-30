package com.github.mrgrtt.ioc.bean_method_test;

import com.github.mrgrtt.ioc.annotation.Bean;
import com.github.mrgrtt.ioc.annotation.Inject;

@Bean
public class BeanA {
    @Inject
    protected BeanB c;

    @Bean
    public static BeanD newBeanD(BeanA a, BeanB b) {
        return new BeanD(a, b);
    }
}
