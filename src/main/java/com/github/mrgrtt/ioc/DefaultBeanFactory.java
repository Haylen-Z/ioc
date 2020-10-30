package com.github.mrgrtt.ioc;

import com.github.mrgrtt.ioc.annotation.Inject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * beanFactory默认实现
 * @author haylen
 * @date 2020-10-25
 */
public class DefaultBeanFactory implements BeanFactory {
    /**
     * type 到 bean name 的映射
     */
    private Map<Class<?>, Set<String>> classBeanNameMap = new ConcurrentHashMap<>();

    /**
     * bean Name 到 bean定义的映射
     */
    private Map<String, BeanDefinition> beanNameBeanDefinitionMap = new ConcurrentHashMap<>();

    /**
     * bean name到单例bean的映射
     */
    private Map<String, Object> singletons = new ConcurrentHashMap<>();

    /**
     * 已创建但未注入的bean，用于检测和解决循环引用
     */
    private Map<String, Object> injectingBeans = new ConcurrentHashMap<>();

    /**
     * 正在解决构造函数依赖的bean，用于检测构造函数循环依赖
     */
    private Set<String> creatingBeans = Collections.newSetFromMap(new ConcurrentHashMap<>());

    private List<BeanPostProcessor> postProcessors = new CopyOnWriteArrayList<>();

    @Override
    public void addBean(Object bean) {
        String beanName = bean.getClass().getName() + "@" + bean.hashCode();
        addBean(beanName, bean);
    }

    @Override
    public void addBean(String beanName, Object bean) {
        if (singletons.containsKey(beanName)) {
            return;
        }
        mapTypeToName(bean.getClass(), beanName);

        singletons.put(beanName, bean);
    }

    private void mapTypeToName(Class<?> cls, String beanName) {
        for (Class<?> type: getAllType(cls)) {
            Set<String> nameSet = classBeanNameMap.get(type);
            if (nameSet == null) {
                nameSet = Collections.newSetFromMap(new ConcurrentHashMap<>());
                classBeanNameMap.put(type, nameSet);
            }
            nameSet.add(beanName);
        }
    }

    private List<Class<?>> getAllType(Class<?> cls) {
        List<Class<?>> types = new ArrayList<>();
        types.add(cls);

        // 获取所有祖先类
        Class<?> superCls = cls.getSuperclass();
        while (superCls != null) {
            types.add(superCls);
            superCls = superCls.getSuperclass();
        }

        types.addAll(Arrays.asList(cls.getInterfaces()));
        return types;
    }

    @Override
    public Object getBean(Class<?> cls) {
        String beanName = getBeanNameByType(cls);
        return beanName == null ? null : getBean(beanName);
    }

    private String getBeanNameByType(Class<?> cls) {
        Set<String> beanNames = classBeanNameMap.get(cls);
        if (beanNames == null ||  beanNames.isEmpty()) {
            return null;
        }
        return beanNames.iterator().next();
    }

    @Override
    public Object getBean(String beanName) {
         if (!beanNameBeanDefinitionMap.containsKey(beanName)) {
             return null;
         }

         BeanDefinition bd = beanNameBeanDefinitionMap.get(beanName);
        // 构造器循环依赖
        if (creatingBeans.contains(beanName)) {
            throw new RuntimeException("存在构造函数循环依赖，依赖的类型：" + bd.getTypeClass().getName());
        }

         Scope scope = bd.getScope();
         if (scope == Scope.PROTOTYPE) {
             return getPrototype(beanName, bd);
         }
         return getSingleton(beanName, bd);
    }

    private Object getPrototype(String beanName, BeanDefinition bd) {
        if (injectingBeans.containsKey(beanName)) {
            throw new RuntimeException("存在原型循环依赖，依赖类型：" + bd.getTypeClass().getName());
        }
        Class<?>[] constructorParamTypes = bd.getCreatorParams();
        Object[] constructorParams = new Object[constructorParamTypes.length];

        creatingBeans.add(beanName);
        // 构造函数依赖注入
        for (int i = 0; i < constructorParamTypes.length; ++i) {
            Class<?> type = constructorParamTypes[i];
            // 检测原型bean间的循环依赖
            String paramBeanName = getBeanNameByType(type);

            Object paramBean = getBean(type);
            Asserts.notNull(paramBean, "不能够解决依赖：" + type.getName());
            constructorParams[i] = paramBean;
        }
        Object bean = bd.getBeanCreator().create(constructorParams);
        creatingBeans.remove(beanName);

        beforePropertySet(bean, bd);
        // 解决@Injec依赖注入
        injectingBeans.put(beanName, bean);
        for (Field field: bd.getDependencyFields()) {
            // 检测原型bean间的循环依赖
            String fieldBeanName = getFieldBeanName(field);
            injectFiledDependency(bean, field, fieldBeanName);
        }
        injectingBeans.remove(beanName);
        bean = afterPropertySet(bean, bd);
        return bean;
    }

    private String getFieldBeanName(Field field) {
        Inject inject = field.getAnnotation(Inject.class);
        String fieldBeanName;
        if (inject.name().length() > 0) {
            fieldBeanName = inject.name();
        } else {
            fieldBeanName = getBeanNameByType(field.getType());
        }
        return fieldBeanName;
    }

    private void injectFiledDependency(Object bean, Field field, String beanName) {
        Object fieldValue = getBean(beanName);
        Asserts.notNull(fieldValue, "不能够解决依赖：" + field.getType().getName());
        field.setAccessible(true);
        try {
            field.set(bean, fieldValue);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private Object getSingleton(String beanName, BeanDefinition bd) {
        if (singletons.containsKey(beanName)) {
            return singletons.get(beanName);
        }
        if (injectingBeans.containsKey(beanName)) {
            return injectingBeans.get(beanName);
        }

        // 解决构造函数依赖
        creatingBeans.add(beanName);

        Class<?>[] paramTypes = bd.getCreatorParams();
        Object[] params = new Object[paramTypes.length];
        for (int i = 0; i < paramTypes.length; ++i) {
            Object paramBean = getBean(paramTypes[i]);
            Asserts.notNull(paramBean, "不能够解决依赖：" + paramTypes[i].getName());
            params[i] = paramBean;
        }

        creatingBeans.remove(beanName);

        // 创建bean
        Object bean = bd.getBeanCreator().create(params);

        beforePropertySet(bean, bd);
        // @Inject字段注入
        injectingBeans.put(beanName, bean);
        for (Field field: bd.getDependencyFields()) {
           injectFiledDependency(bean, field, getFieldBeanName(field));
        }
        injectingBeans.remove(beanName);

        bean = afterPropertySet(bean, bd);
        singletons.put(beanName, bean);
        return bean;
    }

    private void beforePropertySet(Object bean, BeanDefinition bd) {
        for (BeanPostProcessor processor: this.postProcessors) {
            processor.beforeSetProperty(this, bd, bean);
        }
    }

    private Object afterPropertySet(Object bean, BeanDefinition bd) {
        Object processedBean = bean;
        for (BeanPostProcessor processor: this.postProcessors) {
            processedBean =  processor.afterSetProperty(this, bd, bean);
        }
        return processedBean;
    }

    @Override
    public void addBeanPostProcessor(BeanPostProcessor processor) {
        postProcessors.add(processor);
    }

    @Override
    public synchronized void refresh() {
        singletons.clear();
        List<Method> beanMethods = new LinkedList<>();
        for (String beanName: beanNameBeanDefinitionMap.keySet()) {
            if (beanNameBeanDefinitionMap.get(beanName).getScope() == Scope.SINGLETON) {
                getBean(beanName);
            }
        }
    }

    @Override
    public void register(BeanDefinition bd) {
        mapTypeToName(bd.getTypeClass(), bd.getBeanName());
        beanNameBeanDefinitionMap.put(bd.getBeanName(), bd);
    }

    @Override
    public boolean hasBean(Class<?> cls) {
        return !classBeanNameMap.get(cls).isEmpty();
    }

    @Override
    public boolean hasBean(String beanName) {
        return singletons.containsKey(beanName);
    }

    @Override
    public Set<String> listBeanName() {
        return singletons.keySet();
    }

    @Override
    public Collection<Object> listBean() {
        return singletons.values();
    }
}
