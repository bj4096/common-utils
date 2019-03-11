package com.sljl.core.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Map生成工具
 *
 * @author L.Y.F
 */
public class MapHelper {

    // 防创建
    private MapHelper() {}

    public static <K, V> InnerMapBuiler<K, V> builder(K k1, V v1) {
        return new InnerMapBuiler<K, V>(2).put(k1, v1);
    }

    public static <K, V> InnerMapBuiler<K, V> builder(K k1, V v1, K k2, V v2) {
        checkDuple(k1, k2);
        return new InnerMapBuiler<K, V>(4).put(k1, v1).put(k2, v2);
    }

    public static <K, V> InnerMapBuiler<K, V> builder(K k1, V v1, K k2, V v2, K k3, V v3) {
        checkDuple(k1, k2, k3);
        return new InnerMapBuiler<K, V>(8).put(k1, v1).put(k2, v2).put(k3, v3);
    }

    public static <K, V> InnerMapBuiler<K, V> builder(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
        checkDuple(k1, k2, k3, k4);
        return new InnerMapBuiler<K, V>(8).put(k1, v1).put(k2, v2).put(k3, v3).put(k4, v4);
    }

    public static <K, V> InnerMapBuiler<K, V> builder(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
        checkDuple(k1, k2, k3, k4, k5);
        return new InnerMapBuiler<K, V>(16).put(k1, v1).put(k2, v2).put(k3, v3).put(k4, v4).put(k5, v5);
    }

    public static <K, V> InnerMapBuiler<K, V> builder(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6) {
        checkDuple(k1, k2, k3, k4, k5);
        return new InnerMapBuiler<K, V>(16).put(k1, v1).put(k2, v2).put(k3, v3).put(k4, v4).put(k5, v5).put(k6, v6);
    }

    public static <K, V> InnerMapBuiler<K, V> builder(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7) {
        checkDuple(k1, k2, k3, k4, k5);
        return new InnerMapBuiler<K, V>(16).put(k1, v1).put(k2, v2).put(k3, v3).put(k4, v4).put(k5, v5).put(k6, v6).put(k7, v7);
    }

    public static <K, V> InnerMapBuiler<K, V> builder(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8) {
        checkDuple(k1, k2, k3, k4, k5);
        return new InnerMapBuiler<K, V>(16).put(k1, v1).put(k2, v2).put(k3, v3).put(k4, v4).put(k5, v5).put(k6, v6).put(k7, v7).put(k8, v8);
    }

    public static <K, V> InnerMapBuiler<K, V> builder(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9) {
        checkDuple(k1, k2, k3, k4, k5);
        return new InnerMapBuiler<K, V>(16).put(k1, v1).put(k2, v2).put(k3, v3).put(k4, v4).put(k5, v5).put(k6, v6).put(k7, v7).put(k8, v8).put(k9, v9);
    }

    public static <K, V> InnerMapBuiler<K, V> builder(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9, K k10, V v10) {
        checkDuple(k1, k2, k3, k4, k5);
        return new InnerMapBuiler<K, V>(16).put(k1, v1).put(k2, v2).put(k3, v3).put(k4, v4).put(k5, v5).put(k6, v6).put(k7, v7).put(k8, v8).put(k9, v9).put(k10, v10);
    }

    /**
     * 获取一个builder，使用java默认初始容量，支持方法链， 在java无法自动识别正确泛型类别的时候，请使用以下例子的语法：<br/>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; {@code Map<String, Object> map =
     * MapHelper.<String, Object>builder()}
     *
     * @return
     */
    public static <K, V> InnerMapBuiler<K, V> builder() {
        return builder(0);
    }

    /**
     * 检查是否有重复的key，建议用于少量key，当有重复项则抛出异常
     *
     * @param objs
     *
     * @throws IllegalArgumentException 当obj有重复项时
     */
    private static void checkDuple(Object... objs) throws IllegalArgumentException {
        Set<Object> set = new HashSet<Object>(objs.length);
        for (Object object : objs) {
            if (set.contains(object)) {
                throw new IllegalArgumentException("重复key: " + object);
            }
            set.add(object);
        }
    }

    /**
     * 获取一个builder，并指定初始容量，支持方法链， 在java无法自动识别正确泛型类别的时候，请使用以下例子的语法：<br/>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; {@code Map<String, Object> map =
     * MapHelper.<String, Object>builder(8)}
     *
     * @param initSize 初始容量，为0时，使用java默认初始值
     *
     * @return
     */
    public static <K, V> InnerMapBuiler<K, V> builder(int initSize) {
        return new InnerMapBuiler<K, V>(initSize);
    }

    /**
     * 内部构建map的类 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
     *
     * @param <K>
     * @param <V>
     */
    public static class InnerMapBuiler<K, V> {
        private Map<K, V> map;

        /**
         * 使用java默认的初始容量
         *
         * @param initSize
         */
        private InnerMapBuiler(int initSize) {
            if (0 == initSize) {
                map = new HashMap<K, V>();
            } else {
                map = new HashMap<K, V>(initSize);
            }
        }

        /**
         * 当符合条件时添加K,V
         *
         * @param key map的key
         * @param value map的value
         * @param isOk 表达式结果，true则添加，false则忽略K,V，不添加
         *
         * @return this
         */
        public InnerMapBuiler<K, V> put(K key, V value, boolean isOk) {
            if (isOk) {
                return put(key, value);
            }
            return this;
        }

        public InnerMapBuiler<K, V> put(K key, V value) {
            map.put(key, value);
            return this;
        }

        public Map<K, V> build() {
            return new HashMap<K, V>(map);
        }

        @Override
        public String toString() {
            return this.build().toString();
        }
    }

}
