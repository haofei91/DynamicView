package com.benny.library.dynamicview.view;

import android.content.Context;
import android.util.LruCache;

public class DynamicViewBuilderFactory {
    private static final String GENERATED_PACKAGE = "com.benny.library.dynamicview.builder.";


    private static LruCache<String, Class<?>> classCache = new LruCache<>(50);

    /**
     * 【用于构建节点树过程】
     * 1一、获取指定节点名称的DynamicViewBuilder控件构造器Class
     *      1. 该构造器的createView(Context context) 可以 构建对应的 真实UI布局
     *      2. 该构造器的 setProperty(String key, String value) ， 可以在view上设置属性
     * @param name
     * @return
     * @throws Exception
     */
    public static Class<?> register(String name) throws Exception {
        Class<?> clazz = classCache.get(name);
        if (clazz == null) {
            clazz = Class.forName(GENERATED_PACKAGE + name + "$$Builder");
            classCache.put(name, clazz);

            clazz.newInstance();
        }
        return clazz;
    }


    /**
     * 【用于构建视图inflate过程】
     * 二、 实例化构造器，并触发createView(Context context) 可以 构建对应的 真实UI布局
     * @param context
     * @param name
     * @return
     * @throws Exception
     */
    public static DynamicViewBuilder create(Context context, String name) throws Exception {
        Class<?> clazz = register(name);
        DynamicViewBuilder builder = (DynamicViewBuilder) clazz.newInstance();

        builder.createView(context);
        return builder;
    }



}
