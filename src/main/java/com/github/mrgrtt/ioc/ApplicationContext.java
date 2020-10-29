package com.github.mrgrtt.ioc;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;

/**
 * 抽象上下文
 * @author Haylen
 * @date 2020-10-22
 */
public class ApplicationContext implements BeanFactory {
    private BeanFactory beanFactory;

    public ApplicationContext(BeanDefinitionReader beanDefinitionReader) {
        this(new DefaultBeanFactory(), beanDefinitionReader);
    }

    public ApplicationContext(BeanFactory bf, BeanDefinitionReader reader) {
        this.beanFactory = bf;
        loadDefinition(reader);

    }

    private void loadDefinition(BeanDefinitionReader reader) {
        try {
            for (BeanDefinition db: reader.getBeanDefinitions()) {
                beanFactory.register(db);
            }
        } catch (IOException e) {
            throw new RuntimeException("Bean定义解析异常", e);
        }
    }

    @Override
    public Object getBean(Class<?> cls) {
        return beanFactory.getBean(cls);
    }

    @Override
    public Object getBean(String beanName) {
        return beanFactory.getBean(beanName);
    }

    @Override
    public void addBeanPostProcessor(BeanPostProcessor processor) {
        beanFactory.addBean(processor);
    }

    @Override
    public boolean hasBean(Class<?> cls) {
        return beanFactory.hasBean(cls);
    }

    @Override
    public boolean hasBean(String beanName) {
        return beanFactory.hasBean(beanName);
    }

    @Override
    public void addBean(Object bean) {
        beanFactory.addBean(bean);
    }

    @Override
    public void addBean(String beanName, Object bean) {
        beanFactory.addBean(beanName, bean);
    }

    @Override
    public Collection<Object> listBean() {
        return beanFactory.listBean();
    }

    @Override
    public Set<String> listBeanName() {
        return beanFactory.listBeanName();
    }

    @Override
    public void refresh() {
        beanFactory.refresh();
    }

    @Override
    public void register(BeanDefinition bd) {
        beanFactory.register(bd);
    }
}
