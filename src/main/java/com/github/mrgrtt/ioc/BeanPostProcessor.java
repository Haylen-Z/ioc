package com.github.mrgrtt.ioc;

/**
 * bean后置处理器，在bean创建过程中调用
 * @author haylen
 * @date 2020-10-28
 */
public interface BeanPostProcessor {
    /**
     * 在bean实例化之后，属性被注入之前调用
     * @param beanFactory bean工厂
     * @param bean 正在创建的bean
     */
    void beforeSetProperty(BeanFactory beanFactory, BeanDefinition bd, Object bean);

    /**
     * 在bean的属性被注入之后调用
     * @param beanFactory bean工厂
     * @param bean 正在创建的bean
     * @return 经过处理的bean
     */
    Object afterSetProperty(BeanFactory beanFactory, BeanDefinition bd, Object bean);
}
