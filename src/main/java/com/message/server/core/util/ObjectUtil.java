package com.message.server.core.util;

import com.message.server.core.exception.BusinessException;

import java.lang.reflect.Field;
import java.util.*;

/**
 * 对象帮助类
 *
 * @author yangdaxin@lcservis.com
 * @version 创建时间 2018/7/19 09:43
 */
public final class ObjectUtil {
    private ObjectUtil() {
        // 有意留空，不做任何处理
    }

    /**
     * 对象转mpa
     *
     * @param obj
     * @return
     */
    public static Map<String, Object> objectToMap(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            Map<String, Object> map = new HashMap<>(16);
            Field[] declaredFields = obj.getClass().getDeclaredFields();
            for (Field field : declaredFields) {
                field.setAccessible(true);
                map.put(field.getName(), field.get(obj));
            }
            return map;
        } catch (Exception e) {
            throw new BusinessException(e);
        }
    }

    /**
     * 数组去重
     *
     * @param list
     * @param <E>
     * @return
     */
    public static <E> List<E> getDuplicateElements(List<E> list) {
        HashSet h = new HashSet(list);
        List<E> newList = new ArrayList<>(h.size());
        list = null;
        newList.addAll(h);
        return newList;
    }

}
