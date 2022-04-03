package com.farecalulator.processors;

import com.farecalulator.dao.CacheManager;
import com.farecalulator.dao.CacheType;
import com.farecalulator.dao.cache.Cache;
import com.farecalulator.dao.entity.*;
import com.farecalulator.model.Journey;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class PeakOffPeakFareRule extends AbstractFareRuleProcessor {

  private static final Logger LOGGER = Logger.getLogger(PeakOffPeakFareRule.class.getName());

  public PeakOffPeakFareRule(RuleProcessor nextProcessor) {
    super(nextProcessor);
  }

  @Override
  public double process(List<Journey> journeyList, Context context) {
    Map<LocalDate, Path> farthestDailyPath = new HashMap<>();
    for (Journey journey : journeyList) {
      Path path = new Path(journey.getFromZone(), journey.getToZone());
      PeakOffPeakFareData peakOffPeakCache = getPeakOffPeakFareData(path);
      DayOfWeek dayOfWeek = journey.getDate().getDayOfWeek();
      Double fare =
          isPeakHour(dayOfWeek, journey.getTime())
              ? peakOffPeakCache.getPeakFare()
              : peakOffPeakCache.getOffPeakFare();
      journey.setFare(fare);
      populateDailyFarthestPathMap(farthestDailyPath, journey);
    }
    context.setDailyFarthestPath(farthestDailyPath);
    LOGGER.info("===================Peak OffPeak Fare Rule Applied=======================");

    if (hasNextChainAvailable()) {
      return super.process(journeyList, context);
    } else {
      return journeyList.stream().mapToDouble(Journey::getFare).sum();
    }
  }

  private PeakOffPeakFareData getPeakOffPeakFareData(Path path) {
    Cache cache = CacheManager.getInstance().get(CacheType.PEAK_OFF_PEAK_FARE);
    PeakOffPeakFareData peakOffPeakCache = (PeakOffPeakFareData) cache.getData(path);
    return peakOffPeakCache;
  }

  public boolean isPeakHour(DayOfWeek dayOfWeek, LocalTime time) {
    Cache cache = CacheManager.getInstance().get(CacheType.PEAK_TIME);
    PeakTimeData peakTimeData = (PeakTimeData) cache.getData(new PeakTimeKey(dayOfWeek));
    return peakTimeData.getList().stream()
        .anyMatch(
            peakSchedule ->
                time.compareTo(peakSchedule.getStartTime()) >= 0
                    && time.compareTo(peakSchedule.getEndTime()) <= 0);
  }

  private void populateDailyFarthestPathMap(
      Map<LocalDate, Path> farthestDailyPathMap, Journey journey) {
    Path currentPath = new Path(journey.getFromZone(), journey.getToZone());
    if (farthestDailyPathMap.get(journey.getDate()) == null) {
      farthestDailyPathMap.put(journey.getDate(), currentPath);
    } else {
      Cache cache = CacheManager.getInstance().get(CacheType.CAPPED_FARE);
      double currentPathFare = ((CappedFareData) cache.getData(currentPath)).getDailyCap();
      Path existingPath = farthestDailyPathMap.get(journey.getDate());
      double existingPathFare = ((CappedFareData) cache.getData(existingPath)).getDailyCap();
      if (currentPathFare > existingPathFare) {
        farthestDailyPathMap.put(journey.getDate(), currentPath);
      }
    }
  }
}
