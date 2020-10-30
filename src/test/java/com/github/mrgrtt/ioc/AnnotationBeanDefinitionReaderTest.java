package com.github.mrgrtt.ioc;

import com.github.mrgrtt.ioc.bean_reader_test.BeanA;
import com.github.mrgrtt.ioc.bean_reader_test.BeanB;
import com.github.mrgrtt.ioc.bean_reader_test.BeanC;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AnnotationBeanDefinitionReaderTest {

    @Test
    public void test() throws IOException {
        Class<?>[] beanClasses = new Class[] {BeanA.class, BeanB.class, BeanC.class};

        String packageName = "com.github.mrgrtt.ioc.bean_reader_test";
        List<BeanDefinition> dfs = new AnnotationBeanDefinitionReader(packageName).getBeanDefinitions();
        assertEquals(beanClasses.length, dfs.size());

        Map<Class<?>, BeanDefinition> dfMap = new HashMap<Class<?>, BeanDefinition>();
        dfs.forEach((bd) -> {
            dfMap.put(bd.getTypeClass(), bd);
        });

        for (Class<?> c: beanClasses) {
            assertTrue(dfMap.containsKey(c));
            BeanDefinition df = dfMap.get(c);

            if (c == BeanA.class) {
                assertEquals(df.getDependencyFields().size(), 2);
                assertEquals(df.getBeanMethods().size(), 1);
                assertEquals(df.getCreatorParams().length, 0);
            }
            if (c == BeanB.class) {
                assertEquals(df.getDependencyFields().size(), 0);
                assertEquals(df.getBeanMethods().size(), 0);
                assertEquals(df.getCreatorParams().length, 2);
                assertTrue(df.getScope() == Scope.PROTOTYPE);

            }
            if (c == BeanC.class) {
                assertEquals(df.getDependencyFields().size(), 2);
                assertEquals(df.getBeanMethods().size(), 0);
                assertEquals(df.getCreatorParams().length, 0);
                assertEquals(df.getBeanName(), "beanC");
            }
        }
    }

}