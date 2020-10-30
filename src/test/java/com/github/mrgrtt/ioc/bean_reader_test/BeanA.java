package com.github.mrgrtt.ioc.bean_reader_test;

import com.github.mrgrtt.ioc.Scope;
import com.github.mrgrtt.ioc.annotation.Bean;
import com.github.mrgrtt.ioc.annotation.Inject;

@Bean
public class BeanA {
    @Inject
    private BeanB b;
    @Inject
    protected BeanC c;

    @Bean(name = "beanD", scope = Scope.PROTOTYPE)
    public static BeanD newBeanD() {
        return new BeanD();
    }
}
