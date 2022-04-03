package com.farecalulator.dao.loader;

import com.farecalulator.dao.Data;
import com.farecalulator.dao.entity.Path;
import com.farecalulator.dao.entity.PeakOffPeakFareData;

import java.util.*;

public class PeakOffPeakFareDataLoader implements DataLoader<Path, PeakOffPeakFareData> {

  private Map<Path, PeakOffPeakFareData> peakOffPeakFareDataMap = new HashMap<>();

  public PeakOffPeakFareDataLoader() {
    dummyData();
  }

  @Override
  public Map<Path, PeakOffPeakFareData> loadAll() {
    return new HashMap<>(peakOffPeakFareDataMap);
  }

  @Override
  public Data load(Path dataKey) {
    return peakOffPeakFareDataMap.get(dataKey);
  }

  @Override
  public List<PeakOffPeakFareData> load(List<Path> keys) {
    List<PeakOffPeakFareData> resultList = new ArrayList<>();
    peakOffPeakFareDataMap.entrySet().stream()
        .forEach(
            entry -> {
              if (keys.contains(entry.getKey())) {
                resultList.add(entry.getValue());
              }
            });
    return resultList;
  }

  private void dummyData() {
    peakOffPeakFareDataMap.put(new Path(1, 1), new PeakOffPeakFareData(1, 1, 30.00, 25.00));
    peakOffPeakFareDataMap.put(new Path(1, 2), new PeakOffPeakFareData(1, 2, 35.00, 30.00));
    peakOffPeakFareDataMap.put(new Path(2, 1), new PeakOffPeakFareData(2, 1, 35.00, 30.00));
    peakOffPeakFareDataMap.put(new Path(2, 2), new PeakOffPeakFareData(1, 1, 25.00, 20.00));
  }
}
