package com.farecalulator.dao.loader;

import com.farecalulator.dao.Data;
import com.farecalulator.dao.DataKey;

import java.util.List;
import java.util.Map;

public abstract class DataLoader<K extends DataKey, V extends Data> {
  public abstract Map<K, V> loadAll();

  public abstract Data load(K dataKey);

  public abstract List<V> load(List<K> keys);
}
