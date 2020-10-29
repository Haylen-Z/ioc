package com.github.mrgrtt.ioc;

/**
 * bean注册器
 * @author Haylen
 * @date 2020-10-22
 */
public interface BeanRegister {

    /**
     * 注册bean
     * @param bd bean定义
     */
    void register(BeanDefinition bd);
}
