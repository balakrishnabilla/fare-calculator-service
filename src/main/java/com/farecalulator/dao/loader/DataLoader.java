package com.farecalulator.dao.loader;

import com.farecalulator.dao.Data;
import com.farecalulator.dao.DataKey;

import java.util.List;
import java.util.Map;

public interface DataLoader<K extends DataKey, V extends Data> {
  public Map<K, V> loadAll();

  public Data load(K dataKey);

  public List<V> load(List<K> keys);
}
