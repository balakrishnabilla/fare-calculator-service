package com.farecalulator.processors;

import com.farecalulator.dao.CacheManager;
import com.farecalulator.dao.CacheType;
import com.farecalulator.dao.cache.Cache;
import com.farecalulator.dao.entity.CappedFareData;
import com.farecalulator.dao.entity.Path;
import com.farecalulator.model.Journey;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;
/**
 * This class is responsible for applying the daily cap fare rules and adjusting the fares of weekly
 * Journeys And if there WeeklyCapFareRule injected, then it will also calculate the farthest Path
 * for each day that will be required in WeeklyCapFareRule processing.
 */
public class DailyCapFareRule extends AbstractFareRuleProcessor {
  private static final Logger LOGGER = Logger.getLogger(DailyCapFareRule.class.getName());

  public DailyCapFareRule(RuleProcessor nextProcessor) {
    super(nextProcessor);
  }

  @Override
  public double process(List<Journey> journeyList, Context context) {
    if (journeyList.isEmpty()) {
      return 0.0;
    }
    Map<LocalDate, Journey> dailyMap = new HashMap<>();
    Map<Integer, Path> farthestWeeklyPath = new HashMap<>();
    double totalFare = 0.0;
    LocalDate previousDate = null;
    for (Journey journey : journeyList) {
      if (previousDate == null) {
        previousDate = journey.getDate();
      } else if (!previousDate.equals(journey.getDate())) {
        previousDate = journey.getDate();
        totalFare = 0.0;
      }
      adjustFare(totalFare, journey, context);
      totalFare += journey.getFare();
      populateDailyRollupMap(dailyMap, context, journey);
      populateWeeklyFarthestMap(farthestWeeklyPath, journey);
    }
    context.setWeeklyFarthestPath(farthestWeeklyPath);
    List<Journey> dailyRollupList =
        dailyMap.values().stream().sorted().collect(Collectors.toList());
    LOGGER.info("===================Daily Cap Fare Rule Applied=======================");
    dailyRollupList.forEach(journey -> LOGGER.info(journey.toString()));
    return super.process(dailyRollupList, context);
  }

  private void adjustFare(double totalFare, Journey journey, Context context) {
    Path farthestPath = context.getDailyFarthestPath().get(journey.getDate());
    double maxCapFare = getDailyCapFare(farthestPath);
    boolean isTotalFareExceedsCap = Double.sum(journey.getFare(), totalFare) >= maxCapFare;
    if (isTotalFareExceedsCap) {
      journey.setFare(maxCapFare - totalFare);
    }
  }

  private double getDailyCapFare(Path farthestPath) {
    Cache<Path, CappedFareData> cache = CacheManager.getInstance().get(CacheType.CAPPED_FARE);
    return cache.getData(farthestPath).getDailyCap();
  }

  private void populateDailyRollupMap(
      Map<LocalDate, Journey> dailyMap, Context context, Journey journey) {
    Path farthestPath = context.getDailyFarthestPath().get(journey.getDate());
    if (dailyMap.get(journey.getDate()) == null) {
      dailyMap.put(journey.getDate(), new Journey(journey));
    } else {
      rollup(dailyMap, journey, farthestPath);
    }
  }

  private void rollup(Map<LocalDate, Journey> dailyMap, Journey journey, Path farthestPath) {
    Journey rollup = dailyMap.get(journey.getDate());
    rollup.setFromZone(farthestPath.getFromZone());
    rollup.setToZone(farthestPath.getToZone());
    rollup.setFare(journey.getFare() + rollup.getFare());
  }

  private void populateWeeklyFarthestMap(Map<Integer, Path> farthestDailyPath, Journey journey) {
    if (!hasNextChainAvailable()) {
      return;
    }
    Path currentPath = new Path(journey.getFromZone(), journey.getToZone());
    WeekFields weekFields = WeekFields.of(Locale.getDefault());
    int weekNumber = journey.getDate().get(weekFields.weekOfWeekBasedYear());
    if (farthestDailyPath.get(weekNumber) == null) {
      farthestDailyPath.put(weekNumber, currentPath);
    } else {
      Path existingPath = farthestDailyPath.get(weekNumber);
      Cache<Path, CappedFareData> cache = CacheManager.getInstance().get(CacheType.CAPPED_FARE);
      double currentPathFare = cache.getData(currentPath).getWeeklyCap();
      double existingPathFare = cache.getData(existingPath).getWeeklyCap();
      if (currentPathFare > existingPathFare) {
        farthestDailyPath.put(weekNumber, currentPath);
      }
    }
  }
}
