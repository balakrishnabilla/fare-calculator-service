package com.farecalculator.dao.cache;


import com.farecalculator.dao.CacheType;
import com.farecalculator.dao.Data;
import com.farecalculator.dao.DataKey;

/**
 * We can use hazel case here as an in memory distributed cache.
 * @param <K>
 * @param <V>
 */

public interface Cache<K extends DataKey, V extends Data> {
    CacheType getType();
    V getData(K key);
    void putData(K key, V data);
    void loadAll();
    V load(K dataKey);
}
