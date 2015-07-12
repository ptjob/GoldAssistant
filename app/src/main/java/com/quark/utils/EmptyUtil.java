/*
 * 版       权:  Royal.k.peng@gmail.com, All rights reserved
 * 作       者:  Royal
 * 座 右  铭:  Never give up, adhere to in the end.
 */

package com.quark.utils;

import java.util.Collection;
import java.util.Map;

/**
 * 验证类： 1.String , 2.集合, 3.数组.
 *
 * @author Royal
 * @Email Royal.k.peng@gmail.com
 */
public class EmptyUtil {

    /**
     * 检查string是否为空
     *
     * @param str the verify String.
     * @return {@code true} is empty,false is not.
     */
    public static boolean isStringEmpty(String str) {
        return null == str || str.length() < 1;
    }

    /**
     * 检查string是否不为空
     *
     * @param str the verify String.
     * @return {@code true} is not empty,false is empty.
     */
    public static boolean isStringNotEmpty(String str) {
        return !isStringEmpty(str);
    }

    public static <T> boolean isMapEmpty(Map<T, T> map) {
        return null == map || map.isEmpty();
    }

    public static <T> boolean isMapNotEmpty(Map<T, T> map) {
        return !isMapEmpty(map);
    }

    /**
     * 检测集合类是否为空，list,map,set,
     *
     * @param collection will veifyCollections.
     * @return {@code true} this collection is empty,false is not;
     */
    public static <T> boolean isCollectionEmpty(Collection<T> collection) {
        return null == collection || collection.isEmpty();
    }

    /**
     * 检测集合类是否不为空，list,map,set,
     *
     * @param collection
     * @return
     */
    public static <T> boolean isCollectionNotEmpty(Collection<T> collection) {
        return !isCollectionEmpty(collection);
    }

}
