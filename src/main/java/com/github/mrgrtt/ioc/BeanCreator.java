package com.github.mrgrtt.ioc;

/**
 * 创建bean的接口
 * @author  haylen
 * @data 2020-10-30
 */
public interface BeanCreator {
    Object create(Object... params);
}
