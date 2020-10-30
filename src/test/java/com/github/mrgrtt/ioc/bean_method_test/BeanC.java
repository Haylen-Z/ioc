package com.github.mrgrtt.ioc.bean_method_test;

import com.github.mrgrtt.ioc.annotation.Bean;
import com.github.mrgrtt.ioc.annotation.Inject;

@Bean
public class BeanC {
    @Inject
    private BeanD d;

}
