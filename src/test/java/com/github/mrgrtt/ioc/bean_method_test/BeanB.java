package com.github.mrgrtt.ioc.bean_method_test;

import com.github.mrgrtt.ioc.Scope;
import com.github.mrgrtt.ioc.annotation.Bean;

@Bean(scope = Scope.PROTOTYPE)
public class BeanB {
    private BeanA beanA;

    public BeanB(BeanA a) {
        this.beanA = a;
    }
}
