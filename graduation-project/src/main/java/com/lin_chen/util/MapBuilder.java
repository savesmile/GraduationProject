package com.lin_chen.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Map构造器
 * @param <K>
 * @param <V>
 */
public final class MapBuilder<K, V> {
    private static final int NUM2 = 2;
    private Map<K, V> map;

    private MapBuilder() {
        map = new HashMap<>();
    }

    /**
     * create a builder
     *
     * @param <K> K
     * @param <V> V
     * @return
     */
    public static <K, V> MapBuilder<K, V> create() {
        return new MapBuilder<>();
    }

    /**
     * create a builder
     *
     * @param <K>    K
     * @param <V>    V
     * @param kClass kClass
     * @param vClass vClass
     */
    public static <K, V> MapBuilder<K, V> forType(Class<K> kClass, Class<V> vClass) {
        return new MapBuilder<>();
    }

    /**
     * create String Object com.mofang.util.MapBuilder
     *
     * @param k String
     * @param v Object
     * @return
     */
    public static MapBuilder<String, Object> forTypeSO(String k, Object v) {
        return MapBuilder.forType(String.class, Object.class).with(k, v);
    }

    /**
     * create a builder and put a kv pair.
     *
     * @param <K>   K
     * @param <V>   V
     * @param key   key
     * @param value value
     * @return
     */
    public static <K, V> MapBuilder<K, V> createWith(K key, V value) {
        return new MapBuilder<K, V>().with(key, value);
    }

    /**
     * 快速创建一个map
     *
     * @param <K>               K
     * @param <V>               V
     * @param key               必须的key，决定Map的Key类型
     * @param value             必须的value，决定Map的Value类型
     * @param optionalKeyValues 其他的键值对，类型必须保持一致
     * @return UnmodifiableMap
     */
    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> of(K key, V value, Object... optionalKeyValues) {
        MapBuilder<K, V> builder = createWith(key, value);
        if (optionalKeyValues != null) {
            if (optionalKeyValues.length % NUM2 != 0) {
                throw new IllegalArgumentException("key value must be appear in pairs");
            }
            for (int i = 0; i < optionalKeyValues.length; i = i + NUM2) {
                builder.with((K) optionalKeyValues[i], (V) optionalKeyValues[i + 1]);
            }
        }
        return builder.build();
    }


    /**
     * put 键值对
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public MapBuilder<K, V> with(K key, V value) {
        map.put(key, value);
        return this;
    }

    /**
     * 构造Map对象
     *
     * @return 不可再修改的map
     */
    public Map<K, V> build() {
        return Collections.unmodifiableMap(map);
    }

    /**
     * 返回原生Map对象
     *
     * @return
     */
    public Map<K, V> original() {
        return map;
    }

}
