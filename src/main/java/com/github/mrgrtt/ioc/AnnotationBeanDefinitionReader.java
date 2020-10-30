package com.github.mrgrtt.ioc;

import com.github.mrgrtt.ioc.annotation.Bean;
import com.github.mrgrtt.ioc.annotation.Inject;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * bean注册器
 * @author Haylen
 * @date 2020-10-22
 */
public class AnnotationBeanDefinitionReader implements BeanDefinitionReader {
    private String packageName;
    private BeanNameGenerateStrategy nameGenerateStrategy = new DefaultBeanNameGenerateStrategy();

    public AnnotationBeanDefinitionReader(String packageName) {
        this.packageName = packageName;
    }

    @Override
    public List<BeanDefinition> getBeanDefinitions() throws IOException {
        Set<Class<?>> classes = loadClass(this.packageName);
        List<BeanDefinition> bds = new ArrayList<BeanDefinition>();
        for (Class<?> cls: classes) {
            bds.add(createBeanDefinition(cls));
            bds.addAll(createMethodBeanDefinitions(cls));
        }
        return bds;
    }

    private Set<Class<?>> loadClass(String packageName) throws IOException {
        Set<Class<?>> classSet;
        classSet = new HashSet<Class<?>>();
        Enumeration<URL> urls = Thread.currentThread().getContextClassLoader().getResources(packageName.replace(".", "/"));
        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();
            if (url != null) {
                String protocol = url.getProtocol();
                if (protocol.equals("file")) {
                    String packagePath = url.getPath().replaceAll("%20", " ");
                    addClass(classSet, packagePath, packageName);
                } else if (protocol.equals("jar")) {
                    JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
                    if (jarURLConnection != null) {
                        JarFile jarFile = jarURLConnection.getJarFile();
                        if (jarFile != null) {
                            Enumeration<JarEntry> jarEntries = jarFile.entries();
                            while (jarEntries.hasMoreElements()) {
                                JarEntry jarEntry = jarEntries.nextElement();
                                String jarEntryName = jarEntry.getName();
                                if (jarEntryName.endsWith(".class")) {
                                    String className = jarEntryName.substring(0, jarEntryName.lastIndexOf(".")).replaceAll("/", ".");
                                    doAddClass(classSet, className);
                                }
                            }
                        }
                    }
                }
            }
        }
        return classSet;
    }

    private void addClass(Set<Class<?>> classSet, String packagePath, String packageName) {
        File[] files = new File(packagePath).listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return (file.isFile() && file.getName().endsWith(".class")) || file.isDirectory();
            }
        });
        for (File file : files) {
            String fileName = file.getName();
            if (file.isFile()) {
                String className = fileName.substring(0, fileName.lastIndexOf("."));
                if (className != null && className.length() > 0) {
                    className = packageName + "." + className;
                }
                doAddClass(classSet, className);
            } else {
                String subPackagePath = fileName;
                if (!packagePath.isEmpty()) {
                    subPackagePath = packagePath + "/" + subPackagePath;
                }
                String subPackageName = fileName;
                if (!packageName.isEmpty()) {
                    subPackageName = packageName + "." + subPackageName;
                }
                addClass(classSet, subPackagePath, subPackageName);
            }
        }
    }

    private void doAddClass(Set<Class<?>> classSet, String className) {
        Class<?> cls = null;
        try {
            cls = Class.forName(className, true, Thread.currentThread().getContextClassLoader());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }
        Bean bean = cls.getAnnotation(Bean.class);
        if (bean == null) {
            return;
        }
        classSet.add(cls);
    }

    private BeanDefinition createBeanDefinition(Class<?> cls) {
        BeanDefinition df = new BeanDefinition();
        df.setTypeClass(cls);

        Bean bean = cls.getAnnotation(Bean.class);
        df.setScope(bean.scope());
        df.setCreatorParams(cls.getConstructors()[0].getParameterTypes());
        df.setBeanCreator(params -> {
            try {
                return cls.getConstructors()[0].newInstance(params);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        df.setBeanName(nameGenerateStrategy.getBeanName(cls, df, bean));

        List<Field> fields = new ArrayList<Field>();
        for (Field f: cls.getDeclaredFields()) {
            if (f.getAnnotation(Inject.class) != null) {
                fields.add(f);
            }
        }
        df.setDependencyFields(fields);

        return df;
    }

    private List<BeanDefinition> createMethodBeanDefinitions(Class<?> cls) {
        List<BeanDefinition> beanDefinitions = new ArrayList<>();
        for (Method m: cls.getDeclaredMethods()) {
            Bean bean = m.getAnnotation(Bean.class);
            if (bean == null || m.getReturnType() == Void.TYPE) {
                continue;
            }

            BeanDefinition bd = new BeanDefinition();
            bd.setBeanName(nameGenerateStrategy.getBeanName(m.getReturnType(), bd, bean));
            bd.setTypeClass(m.getReturnType());
            bd.setScope(bean.scope());
            bd.setDependencyFields(new ArrayList<>());
            bd.setCreatorParams(m.getParameterTypes());
            bd.setBeanCreator(params -> {
                try {
                    return m.invoke(null, params);
                } catch (NullPointerException e) {
                    throw new RuntimeException("@bean注解修饰方法时，只能修饰静态方法。");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            beanDefinitions.add(bd);
        }
        return beanDefinitions;
    }
}
