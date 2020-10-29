package com.github.mrgrtt.ioc.constructor_circle_test;

import com.github.mrgrtt.ioc.annotation.Bean;
import com.github.mrgrtt.ioc.annotation.Inject;

@Bean(name = "beanC")
public class BeanC {
    @Inject
    public BeanB b;

    public BeanC(BeanA beanA) {
    }
}
