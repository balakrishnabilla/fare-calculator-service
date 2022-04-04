package com.farecalculator.dao.cache;

import com.farecalculator.dao.CacheType;
import com.farecalculator.dao.entity.CappedFareData;
import com.farecalculator.dao.entity.Path;
import com.farecalculator.dao.loader.DataLoader;

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
