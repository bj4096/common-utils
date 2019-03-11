package com.sljl.core.utils;

import org.apache.commons.collections4.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * 对象中处理NUll的工具类
 *
 * @author L.Y.F
 */
public class NullObjUtil {

    /**
     * 校验传入参数arg是否为空，不为空返回arg，为null返回defaultValue
     *
     * @param arg
     * @param defaultValue
     *
     * @return
     */
    public static <T> T null2Blank(T arg, T defaultValue) {
        if (arg == null) {
            return defaultValue;
        }
        return arg;
    }

    /**
     * 确保此方法过滤的List一定不为null
     *
     * @param origList 原始list
     *
     * @return
     */
    protected <T> List<T> getRidOfNullList(List<T> origList) {
        return CollectionUtils.isEmpty(origList) ? Collections.<T>emptyList() : origList;
    }

    /**
     * 确保此方法过滤的Set一定不为null
     *
     * @param origSet 原始Set
     *
     * @return
     */
    protected <T> Set<T> getRidOfNullSet(Set<T> origSet) {
        return CollectionUtils.isEmpty(origSet) ? Collections.<T>emptySet() : origSet;
    }

    /**
     * 确保此方法过滤的Map一定不为null
     *
     * @param origMap 原始Map
     *
     * @return
     */
    protected <K, V> Map<K, V> getRidOfNullMap(Map<K, V> origMap) {
        return null == origMap ? Collections.<K, V>emptyMap() : origMap;
    }

}
