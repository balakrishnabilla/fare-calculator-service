package com.farecalulator.dao.cache;

import com.farecalulator.dao.CacheType;
import com.farecalulator.dao.entity.CappedFareData;
import com.farecalulator.dao.entity.Path;
import com.farecalulator.dao.loader.DataLoader;

public class CappedFareCache extends AbstractCache<Path, CappedFareData> {

  public CappedFareCache(DataLoader<Path, CappedFareData> dataLoader) {
    super(dataLoader);
  }

  @Override
  public void createDataMap() {
    this.setData(this.getDataLoader().loadAll());
  }

  @Override
  public CacheType getType() {
    return CacheType.CAPPED_FARE;
  }

  @Override
  public CappedFareData getData(Path key) {
    return getData().get(key);
  }

  @Override
  public void putData(Path key, CappedFareData data) {
    getData().put(key, data);
  }

  @Override
  public void loadAll() {
    this.setData(this.getDataLoader().loadAll());
  }

  @Override
  public CappedFareData load(Path dataKey) {
    return (CappedFareData) this.getDataLoader().load(dataKey);
  }
}
