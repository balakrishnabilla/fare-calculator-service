package com.farecalulator.dao.cache;

import com.farecalulator.dao.Data;
import com.farecalulator.dao.DataKey;
import com.farecalulator.dao.loader.DataLoader;

import java.util.Map;

public abstract class AbstractCache<K extends DataKey, V extends Data> implements Cache<K, V> {
    private DataLoader<K,V> dataLoader;
    private Map<K, V> data;

    protected AbstractCache(DataLoader<K, V> dataLoader) {
        this.dataLoader = dataLoader;
    }

    protected abstract void createDataMap();

    public Map<K, V> getData() {
        return data;
    }

    public void setData(Map<K, V> data) {
        this.data = data;
    }

    protected boolean isExist(K key) {
        return data.containsKey(key);
    }


    protected V loadOnDemand(K key) {
        return this.load(key);
    }

    public DataLoader<K, V> getDataLoader() {
        return dataLoader;
    }
}
