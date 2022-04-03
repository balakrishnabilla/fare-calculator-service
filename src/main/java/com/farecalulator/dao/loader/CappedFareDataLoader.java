package com.farecalulator.dao.loader;

import com.farecalulator.dao.Data;
import com.farecalulator.dao.entity.CappedFareData;
import com.farecalulator.dao.entity.Path;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CappedFareDataLoader implements DataLoader<Path, CappedFareData> {

  private Map<Path, CappedFareData> cappedFareDataMap = new HashMap<>();

  public CappedFareDataLoader() {
    dummyData();
  }

  @Override
  public Map<Path, CappedFareData> loadAll() {
    return new HashMap<>(cappedFareDataMap);
  }

  @Override
  public Data load(Path dataKey) {
    return cappedFareDataMap.get(dataKey);
  }

  @Override
  public List<CappedFareData> load(List<Path> keys) {
    List<CappedFareData> resultList = new ArrayList<>();
    cappedFareDataMap.entrySet().stream()
        .forEach(
            entry -> {
              if (keys.contains(entry.getKey())) {
                resultList.add(entry.getValue());
              }
            });
    return resultList;
  }

  private void dummyData() {
    cappedFareDataMap.put(new Path(1, 1), new CappedFareData(1, 1, 100.00, 500.00));
    cappedFareDataMap.put(new Path(1, 2), new CappedFareData(1, 2, 120.00, 600.00));
    cappedFareDataMap.put(new Path(2, 1), new CappedFareData(2, 1, 120.00, 600.00));
    cappedFareDataMap.put(new Path(2, 2), new CappedFareData(1, 1, 80.00, 400.00));
  }
}
