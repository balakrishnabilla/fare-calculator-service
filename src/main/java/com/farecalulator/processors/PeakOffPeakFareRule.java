package com.farecalulator.processors;

import com.farecalulator.dao.CacheManager;
import com.farecalulator.dao.CacheType;
import com.farecalulator.dao.cache.Cache;
import com.farecalulator.dao.entity.CappedFareData;
import com.farecalulator.dao.entity.Path;
import com.farecalulator.dao.entity.PeakOffPeakFareData;
import com.farecalulator.dao.entity.PeakTimeData;
import com.farecalulator.dao.entity.PeakTimeKey;
import com.farecalulator.model.Journey;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * This class is responsible for setting the Peak and Off peak fares for the collection of Journey
 * And if there is DailyCapFareRule injected, then it will also calculate the farthest Path for each day will be required in
 * DailyCapFareRule processing.
 */
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
    return super.process(journeyList, context);
  }

  private PeakOffPeakFareData getPeakOffPeakFareData(Path path) {
    Cache<Path, PeakOffPeakFareData> cache =
        CacheManager.getInstance().get(CacheType.PEAK_OFF_PEAK_FARE);
    return cache.getData(path);
  }

  public boolean isPeakHour(DayOfWeek dayOfWeek, LocalTime time) {
    Cache<PeakTimeKey, PeakTimeData> cache = CacheManager.getInstance().get(CacheType.PEAK_TIME);
    PeakTimeData peakTimeData = cache.getData(new PeakTimeKey(dayOfWeek));
    return peakTimeData.getList().stream()
        .anyMatch(
            peakSchedule ->
                time.compareTo(peakSchedule.getStartTime()) >= 0
                    && time.compareTo(peakSchedule.getEndTime()) <= 0);
  }

  private void populateDailyFarthestPathMap(
      Map<LocalDate, Path> farthestDailyPathMap, Journey journey) {
    if (!hasNextChainAvailable()) {
      return;
    }
    Path currentPath = new Path(journey.getFromZone(), journey.getToZone());
    if (farthestDailyPathMap.get(journey.getDate()) == null) {
      farthestDailyPathMap.put(journey.getDate(), currentPath);
    } else {
      Cache<Path, CappedFareData> cache = CacheManager.getInstance().get(CacheType.CAPPED_FARE);
      double currentPathFare = cache.getData(currentPath).getDailyCap();
      Path existingPath = farthestDailyPathMap.get(journey.getDate());
      double existingPathFare = cache.getData(existingPath).getDailyCap();
      if (currentPathFare > existingPathFare) {
        farthestDailyPathMap.put(journey.getDate(), currentPath);
      }
    }
  }
}
