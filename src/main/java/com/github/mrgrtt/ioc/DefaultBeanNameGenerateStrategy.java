package com.github.mrgrtt.ioc;

import com.github.mrgrtt.ioc.annotation.Bean;

/**
 * bean name 默认生成策略
 * @author haylen
 * @date 2020-10-30
 */
public class DefaultBeanNameGenerateStrategy implements BeanNameGenerateStrategy{
    @Override
    public String getBeanName(Class<?> cls, BeanDefinition bd, Bean bean) {
        String beanName = bean.name();
        if ("".equals(beanName)) {
            beanName = cls.getName() + "@" + bd.hashCode();
        }
        return beanName;
    }

    @Override
    public String getBeanName(Object bean) {
        return bean.getClass().getName() + "@" + bean.hashCode();
    }
}
