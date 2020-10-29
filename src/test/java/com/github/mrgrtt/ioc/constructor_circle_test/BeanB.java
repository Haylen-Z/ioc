package com.github.mrgrtt.ioc.constructor_circle_test;

import com.github.mrgrtt.ioc.Scope;
import com.github.mrgrtt.ioc.annotation.Bean;
import lombok.Getter;

@Bean(scope = Scope.PROTOTYPE)
@Getter
public class BeanB {
    private BeanA beanA;

    public BeanB(BeanA a) {
        this.beanA = a;
    }
}
