package com.github.mrgrtt.ioc;

import java.util.Collection;
import java.util.Set;

/**
 * bean工厂
 * @author Haylen
 * @date 2020-10-22
 */
public interface BeanFactory extends BeanRegister {
    void addBean(Object bean);

    void addBean(String beanName, Object bean);

    boolean hasBean(Class<?> cls);

    boolean hasBean(String beanName);

    Set<String> listBeanName();

    Collection<Object> listBean();

    Object getBean(Class<?> cls);

    Object getBean(String beanName);

    void addBeanPostProcessor(BeanPostProcessor processor);

    void refresh();
}
