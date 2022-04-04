package com.farecalculator.dao.loader;

import com.farecalculator.dao.Data;
import com.farecalculator.dao.entity.PeakTimeKey;
import com.farecalculator.dao.entity.PeakTimeData;
import com.farecalculator.model.*;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.*;

public class PeakTimeDataLoader implements DataLoader<PeakTimeKey, PeakTimeData> {
  private Map<PeakTimeKey, PeakTimeData> peakTimeDataMap = new HashMap<>();

  public PeakTimeDataLoader() {
    dummyData();
  }

  @Override
  public Map<PeakTimeKey, PeakTimeData> loadAll() {
    return new HashMap<>(peakTimeDataMap);
  }

  @Override
  public Data load(PeakTimeKey dataKey) {
    return peakTimeDataMap.get(dataKey);
  }

  @Override
  public List<PeakTimeData> load(List<PeakTimeKey> keys) {
    List<PeakTimeData> resultList = new ArrayList<>();
    peakTimeDataMap.entrySet().stream()
        .forEach(
            entry -> {
              if (keys.contains(entry.getKey())) {
                resultList.add(entry.getValue());
              }
            });
    return resultList;
  }

  /** This is to load the master data in the map. Ideally this should be loaded from DB */
  private void dummyData() {

    List<PeakSchedule> weekDaySchedules =
        Arrays.asList(
            new PeakSchedule(LocalTime.of(7, 0), LocalTime.of(10, 30)),
            new PeakSchedule(LocalTime.of(17, 0), LocalTime.of(20, 0)));

    peakTimeDataMap.put(new PeakTimeKey(DayOfWeek.MONDAY), new PeakTimeData(weekDaySchedules));
    peakTimeDataMap.put(new PeakTimeKey(DayOfWeek.TUESDAY), new PeakTimeData(weekDaySchedules));
    peakTimeDataMap.put(new PeakTimeKey(DayOfWeek.WEDNESDAY), new PeakTimeData(weekDaySchedules));
    peakTimeDataMap.put(new PeakTimeKey(DayOfWeek.THURSDAY), new PeakTimeData(weekDaySchedules));
    peakTimeDataMap.put(new PeakTimeKey(DayOfWeek.FRIDAY), new PeakTimeData(weekDaySchedules));

    List<PeakSchedule> weekEndSchedules =
        Arrays.asList(
            new PeakSchedule(LocalTime.of(9, 0), LocalTime.of(11, 0)),
            new PeakSchedule(LocalTime.of(18, 0), LocalTime.of(22, 0)));
    peakTimeDataMap.put(new PeakTimeKey(DayOfWeek.SATURDAY), new PeakTimeData(weekEndSchedules));
    peakTimeDataMap.put(new PeakTimeKey(DayOfWeek.SUNDAY), new PeakTimeData(weekEndSchedules));
  }
}
