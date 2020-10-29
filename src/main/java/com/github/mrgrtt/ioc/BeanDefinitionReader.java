package com.github.mrgrtt.ioc;

import java.io.IOException;
import java.util.List;

/**
 * bean定义读取器
 * @author Haylen
 * @date 2020-10-22
 */
public interface BeanDefinitionReader {
    /**
     * 获取bean定义
     */
    List<BeanDefinition> getBeanDefinitions() throws IOException;
}
