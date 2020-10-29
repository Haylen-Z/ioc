package com.github.mrgrtt.ioc.constructor_circle_test;

import com.github.mrgrtt.ioc.annotation.Bean;
import com.github.mrgrtt.ioc.annotation.Inject;

@Bean
public class BeanA {
    @Inject
    private BeanB b;

    public BeanA(BeanC beanC){}
}
