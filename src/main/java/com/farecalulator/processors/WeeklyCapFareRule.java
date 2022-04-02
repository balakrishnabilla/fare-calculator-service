package com.farecalulator.processors;

import com.farecalulator.model.Journey;
import com.farecalulator.model.Path;
import com.farecalulator.utils.DateTimeUtil;
import com.farecalulator.utils.FareUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

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
      Double maxCapFare = FareUtil.getWeeklyCap(farthestPath);

      boolean isTotalFareExceedsCap = (journey.getFare() + totalFare) >= maxCapFare;
      if (isTotalFareExceedsCap) {
        journey.setFare(maxCapFare - totalFare);
      }
      totalFare += journey.getFare();

      populateWeeklyRollupMap(weeklyMap, farthestPath, journey);
    }
    weeklyMap.values().forEach(journey -> LOGGER.info(journey.toString()));
    return weeklyMap.values().stream().map(Journey::getFare).reduce(0.0, Double::sum);
  }

  private void populateWeeklyRollupMap(
      Map<Integer, Journey> weeklyMap, Path farthestPath, Journey journey) {
    int weekNumber = DateTimeUtil.getWeekNumber(journey.getDate());
    Journey existingJourney = weeklyMap.get(weekNumber);
    if (existingJourney == null) {
      weeklyMap.put(weekNumber, journey.clone());
    } else {
      Journey rollup = weeklyMap.get(weekNumber);
      rollup.setFromZone(farthestPath.getFromZone());
      rollup.setToZone(farthestPath.getToZone());
      rollup.setFare(journey.getFare() + rollup.getFare());
    }
  }
}
