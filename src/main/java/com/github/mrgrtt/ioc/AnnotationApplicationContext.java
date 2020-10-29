package com.github.mrgrtt.ioc;

/**
 * 注解应用上下文
 * @author haylen
 * @date 2020-10-27
 */
public class AnnotationApplicationContext extends ApplicationContext {

    public AnnotationApplicationContext(String packageName) {
        super(new AnnotationBeanDefinitionReader(packageName));
        super.refresh();
    }
}
