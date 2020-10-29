package com.github.mrgrtt.ioc.normal_bean_test;


import com.github.mrgrtt.ioc.annotation.Bean;
import com.github.mrgrtt.ioc.annotation.Inject;
import lombok.Getter;

@Bean
@Getter
public class BeanA {
    @Inject
    private BeanB beanB;
    @Inject
    private BeanC beanC;
}
