package com.github.mrgrtt.ioc.normal_bean_test;

import com.github.mrgrtt.ioc.annotation.Bean;
import com.github.mrgrtt.ioc.annotation.Inject;
import lombok.Getter;

@Bean
@Getter
public class BeanC {
    @Inject
    private BeanA beanA;
    @Inject
    private BeanB beanB;
    @Inject
    private BeanD beanD;
    @Inject(name = "e2")
    private Ie e;

    public BeanC(BeanD beanD) {

    }
}
