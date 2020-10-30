package com.github.mrgrtt.ioc;

import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Field;
import java.util.List;

/**
 * bean定义数据类
 * @author Haylen
 * @date 2020-10-22
 */
@Getter
@Setter
public class BeanDefinition {
    private Class<?> typeClass;
    private List<Field> dependencyFields;
    private Class<?>[] creatorParams;
    private BeanCreator beanCreator;
    private String beanName;
    private Scope scope;
}
