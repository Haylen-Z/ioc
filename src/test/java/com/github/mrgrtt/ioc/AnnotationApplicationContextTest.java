package com.github.mrgrtt.ioc;

import com.github.mrgrtt.ioc.normal_bean_test.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.*;

class AnnotationApplicationContextTest {

    @Test
    void normalTest() {
        ApplicationContext context =
                new AnnotationApplicationContext("com.github.mrgrtt.ioc.normal_bean_test");

        BeanA beanA = (BeanA) context.getBean(BeanA.class);
        BeanB beanB = (BeanB) context.getBean(BeanB.class);
        BeanC beanC = (BeanC) context.getBean(BeanC.class);
        BeanD beanD = (BeanD) context.getBean(BeanD.class);


        assertNotNull(beanA.getBeanC());
        assertNotNull(beanB.getBeanA());
        assertNotNull(beanB.getBeanC());
        assertNotNull(beanC.getBeanA());
        assertNotNull(beanC.getBeanD());


        assertSame(beanA.getBeanC(), beanB.getBeanC());
        assertSame(beanB.getBeanA(), beanC.getBeanA());
        assertSame(beanC.getE().getClass(), BeanE2.class);
        assertSame(beanD.getE().getClass(), BeanE1.class);

        assertNotSame(beanB, context.getBean(BeanB.class));

    }

    @Test
    void constructorCircleTest() {
        assertThrows(RuntimeException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                ApplicationContext context =
                        new AnnotationApplicationContext("com.github.mrgrtt.ioc.constructor_circle_test");
            }
        });
    }

    @Test
    void prototypeCircleTest() {
        assertThrows(RuntimeException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                    ApplicationContext context =
                            new AnnotationApplicationContext("com.github.mrgrtt.ioc.prototype_circle_test");
                    context.getBean(com.github.mrgrtt.ioc.prototype_circle_test.BeanB.class);
            }
        });
    }

    @Test
    void beanMethodTest() {
        ApplicationContext context =
                new AnnotationApplicationContext("com.github.mrgrtt.ioc.bean_method_test");
        assertNotNull(context.getBean(com.github.mrgrtt.ioc.bean_method_test.BeanD.class));
    }
}