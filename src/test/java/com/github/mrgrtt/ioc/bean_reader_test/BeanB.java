package com.github.mrgrtt.ioc.bean_reader_test;

import com.github.mrgrtt.ioc.Scope;
import com.github.mrgrtt.ioc.annotation.Bean;

@Bean(scope = Scope.PROTOTYPE)
public class BeanB {
    private BeanA beanA;
    private BeanB beanB;

    public BeanB(BeanA a, BeanB b) {
        this.beanA = a;
        this.beanB = b;
    }
}
