package com.github.mrgrtt.ioc.prototype_circle_test;


import com.github.mrgrtt.ioc.Scope;
import com.github.mrgrtt.ioc.annotation.Bean;
import com.github.mrgrtt.ioc.annotation.Inject;

@Bean(scope = Scope.PROTOTYPE)
public class BeanB {
    @Inject
    private BeanC beanC;
}
