package com.farecalulator.processors;

import com.farecalulator.dao.CacheManager;
import com.farecalulator.dao.CacheType;
import com.farecalulator.dao.cache.Cache;
import com.farecalulator.dao.entity.CappedFareData;
import com.farecalulator.dao.entity.Path;
import com.farecalulator.model.Journey;
import com.farecalulator.utils.DateTimeUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
/**
 * This class is responsible for applying the weekly cap fare rules and adjusting the fares of
 * weekly Journeys And if there MonthlyCapFareRule injected, then it will also calculate the
 * farthest Path for each week that will be required in MonthlyCapFareRule processing.
 */
public class WeeklyCapFareRule extends AbstractFareRuleProcessor {
  private static final Logger LOGGER = Logger.getLogger(WeeklyCapFareRule.class.getName());

  public WeeklyCapFareRule(RuleProcessor nextProcessor) {
    super(nextProcessor);
  }

  @Override
  public double process(List<Journey> dailyJourneyList, Context context) {
    if (dailyJourneyList.isEmpty()) {
      return 0.0;
    }
    Map<Integer, Journey> weeklyMap = new HashMap<>();
    double totalFare = 0.0;
    Integer prevWeekNumber = null;

    for (Journey journey : dailyJourneyList) {
      Integer currentWeekNumber = DateTimeUtil.getWeekNumber(journey.getDate());
      if (prevWeekNumber == null) {
        prevWeekNumber = currentWeekNumber;
      } else if (!prevWeekNumber.equals(currentWeekNumber)) {
        prevWeekNumber = currentWeekNumber;
        totalFare = 0.0;
      }
      Path farthestPath = context.getWeeklyFarthestPath().get(currentWeekNumber);
      adjustFare(totalFare, journey, farthestPath);
      totalFare += journey.getFare();
      populateWeeklyRollupMap(weeklyMap, farthestPath, journey);
    }
    weeklyMap.values().forEach(journey -> LOGGER.info(journey.toString()));
    return super.process(new ArrayList<>(weeklyMap.values()), context);
  }

  private void adjustFare(double totalFare, Journey journey, Path farthestPath) {
    Double maxCapFare = getWeeklyCapFare(farthestPath);
    boolean isTotalFareExceedsCap = (journey.getFare() + totalFare) >= maxCapFare;
    if (isTotalFareExceedsCap) {
      journey.setFare(maxCapFare - totalFare);
    }
  }

  private Double getWeeklyCapFare(Path farthestPath) {
    Cache<Path, CappedFareData> cache = CacheManager.getInstance().get(CacheType.CAPPED_FARE);
    return cache.getData(farthestPath).getWeeklyCap();
  }

  private void populateWeeklyRollupMap(
      Map<Integer, Journey> weeklyMap, Path farthestPath, Journey journey) {
    int weekNumber = DateTimeUtil.getWeekNumber(journey.getDate());
    Journey existingJourney = weeklyMap.get(weekNumber);
    if (existingJourney == null) {
      weeklyMap.put(weekNumber, new Journey(journey));
    } else {
      rollup(weeklyMap, farthestPath, journey, weekNumber);
    }
  }

  private void rollup(
      Map<Integer, Journey> weeklyMap, Path farthestPath, Journey journey, int weekNumber) {
    Journey rollup = weeklyMap.get(weekNumber);
    rollup.setFromZone(farthestPath.getFromZone());
    rollup.setToZone(farthestPath.getToZone());
    rollup.setFare(journey.getFare() + rollup.getFare());
  }
}
