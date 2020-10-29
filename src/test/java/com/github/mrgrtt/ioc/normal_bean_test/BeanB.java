package com.github.mrgrtt.ioc.normal_bean_test;

import com.github.mrgrtt.ioc.Scope;
import com.github.mrgrtt.ioc.annotation.Bean;
import com.github.mrgrtt.ioc.annotation.Inject;
import lombok.Getter;

@Bean(scope = Scope.PROTOTYPE)
@Getter
public class BeanB {
    @Inject
    private BeanA beanA;
    @Inject
    private BeanC beanC;
}
