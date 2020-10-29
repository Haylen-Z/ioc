package com.github.mrgrtt.ioc.bean_reader_test;

import com.github.mrgrtt.ioc.annotation.Bean;
import com.github.mrgrtt.ioc.annotation.Inject;

@Bean(name = "beanC")
public class BeanC {
    @Inject
    private BeanA a;

    @Inject
    public BeanB b;


}
