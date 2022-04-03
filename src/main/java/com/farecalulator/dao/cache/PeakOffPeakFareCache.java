package com.farecalulator.dao.cache;

import com.farecalulator.dao.CacheType;
import com.farecalulator.dao.cache.AbstractCache;
import com.farecalulator.dao.entity.Path;
import com.farecalulator.dao.entity.PeakOffPeakFareData;
import com.farecalulator.dao.loader.DataLoader;

public class PeakOffPeakFareCache extends AbstractCache<Path, PeakOffPeakFareData> {

  public PeakOffPeakFareCache(DataLoader<Path, PeakOffPeakFareData> dataLoader) {
    super(dataLoader);
  }

  @Override
  public void createDataMap() {
    this.setData(this.getDataLoader().loadAll());
  }

  @Override
  public CacheType getType() {
    return CacheType.PEAK_OFF_PEAK_FARE;
  }

  @Override
  public PeakOffPeakFareData getData(Path key) {
    return getData().get(key);
  }

  @Override
  public void putData(Path key, PeakOffPeakFareData data) {
    getData().put(key, data);
  }

  @Override
  public void loadAll() {
    this.setData(this.getDataLoader().loadAll());
  }

  @Override
  public PeakOffPeakFareData load(Path dataKey) {
    return (PeakOffPeakFareData) this.getDataLoader().load(dataKey);
  }
}
