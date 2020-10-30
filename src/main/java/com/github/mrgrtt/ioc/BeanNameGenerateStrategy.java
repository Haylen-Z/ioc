package com.github.mrgrtt.ioc;

import com.github.mrgrtt.ioc.annotation.Bean;

/**
 * 创建bean name的策略接口
 * @author  haylen
 * @data 2020-10-30
 */
public interface BeanNameGenerateStrategy {
    String getBeanName(Class<?> cls, BeanDefinition bd, Bean bean);

    String getBeanName(Object bean);
}
