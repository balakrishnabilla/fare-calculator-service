package com.farecalulator.processors;

import com.farecalulator.model.Journey;
import com.farecalulator.model.Path;
import com.farecalulator.utils.FareUtil;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class PeakOffPeakFareRule extends AbstractFareRuleProcessor {
  private static final Logger LOGGER =
          Logger.getLogger(PeakOffPeakFareRule.class.getName());

  public PeakOffPeakFareRule(RuleProcessor nextProcessor) {
    super(nextProcessor);
  }

  @Override
  public double process(List<Journey> journeyList, Context context) {
    Map<LocalDate, Path> farthestDailyPath = new HashMap<>();
    for (Journey journey : journeyList) {
      Path path = new Path(journey.getFromZone(), journey.getToZone());
      DayOfWeek dayOfWeek = journey.getDate().getDayOfWeek();
      Double fare =
          FareUtil.isPeakHour(dayOfWeek, journey.getTime())
              ? FareUtil.getPeakHourFare(path)
              : FareUtil.getOffPeakHourFare(path);
      journey.setFare(fare);
      populateDailyFarthestMap(farthestDailyPath, journey);
    }
    context.setDailyFarthestPath(farthestDailyPath);
    LOGGER.info("Peak OffPeak Fare Rule Applied");
    for (Journey journey : journeyList){
      LOGGER.info(journey.toString());
    }
    if (hasNextChainAvailable()) {
      return super.process(journeyList, context);
    } else {
      return journeyList.stream().mapToDouble(Journey::getFare).sum();
    }
  }

  private void populateDailyFarthestMap(Map<LocalDate, Path> farthestDailyPath, Journey journey) {
    Path currentPath = new Path(journey.getFromZone(), journey.getToZone());
    if (farthestDailyPath.get(journey.getDate()) == null) {
      farthestDailyPath.put(journey.getDate(), currentPath);
    } else {
      Path existingPath = farthestDailyPath.get(journey.getDate());
      if (FareUtil.getDailyCap(currentPath) > FareUtil.getDailyCap(existingPath)) {
        farthestDailyPath.put(journey.getDate(), currentPath);
      }
    }
  }
}
