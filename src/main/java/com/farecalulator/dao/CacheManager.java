package com.farecalulator.dao;

import com.farecalulator.dao.cache.Cache;

import java.util.EnumMap;

public class CacheManager {

  private static final CacheManager INSTANCE = new CacheManager();

  private EnumMap<CacheType, Cache<? extends DataKey, ? extends Data>> caches =
      new EnumMap<>(CacheType.class);

  private CacheManager() {}

  public static CacheManager getInstance() {
    return INSTANCE;
  }

  public void registerCache(Cache<? extends DataKey, ? extends Data> cache) {
    caches.put(cache.getType(), cache);
  }

  public Cache get(CacheType cacheType) {
    return caches.get(cacheType);
  }
}
