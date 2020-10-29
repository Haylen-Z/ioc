package com.github.mrgrtt.ioc;

/**
 * 断言工具类
 * @author haylen
 * @date 2020-10-28
 */
public final class Asserts {

    public static void notNull(Object o, String msg) {
        if (o == null) {
            throw new NullPointerException(msg);
        }
    }

    private Asserts(){}
}
