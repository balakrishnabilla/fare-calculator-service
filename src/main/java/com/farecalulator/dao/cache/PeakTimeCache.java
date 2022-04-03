package com.farecalulator.dao.cache;

import com.farecalulator.dao.CacheType;
import com.farecalulator.dao.entity.PeakTimeKey;
import com.farecalulator.dao.entity.PeakTimeData;
import com.farecalulator.dao.loader.DataLoader;

public class PeakTimeCache extends AbstractCache<PeakTimeKey, PeakTimeData> {

  public PeakTimeCache(DataLoader<PeakTimeKey, PeakTimeData> dataLoader) {
    super(dataLoader);
  }

  @Override
  public void createDataMap() {
    this.setData(this.getDataLoader().loadAll());
  }

  @Override
  public CacheType getType() {
    return CacheType.PEAK_TIME;
  }

  @Override
  public PeakTimeData getData(PeakTimeKey key) {
    return getData().get(key);
  }

  @Override
  public void putData(PeakTimeKey key, PeakTimeData data) {
    getData().put(key, data);
  }

  @Override
  public void loadAll() {
    this.setData(this.getDataLoader().loadAll());
  }

  @Override
  public PeakTimeData load(PeakTimeKey dataKey) {
    return (PeakTimeData) this.getDataLoader().load(dataKey);
  }
}
