package com.farecalulator.dao;

import com.farecalulator.dao.cache.Cache;

import java.util.HashMap;
import java.util.Map;

public class CacheManager {

  private static final CacheManager INSTANCE = new CacheManager();

  private Map<CacheType, Cache> caches = new HashMap<>();

  private CacheManager() {}

  public static CacheManager getInstance() {
    return INSTANCE;
  }

  public void registerCache(Cache cache) {
    caches.put(cache.getType(), cache);
  }

  public Cache get(CacheType cacheType) {
    return caches.get(cacheType);
  }
}
