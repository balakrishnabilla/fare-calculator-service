package com.farecalulator.dao.cache;


import com.farecalulator.dao.CacheType;
import com.farecalulator.dao.Data;
import com.farecalulator.dao.DataKey;

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
