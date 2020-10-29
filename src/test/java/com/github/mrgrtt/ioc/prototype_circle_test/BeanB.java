package com.github.mrgrtt.ioc.prototype_circle_test;


import com.github.mrgrtt.ioc.annotation.Bean;
import com.github.mrgrtt.ioc.annotation.Inject;

@Bean
public class BeanB {
    @Inject
    private BeanC beanC;
}
