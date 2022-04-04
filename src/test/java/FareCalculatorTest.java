import com.farecalculator.dao.*;
import com.farecalculator.dao.cache.CappedFareCache;
import com.farecalculator.dao.cache.PeakOffPeakFareCache;
import com.farecalculator.dao.cache.PeakTimeCache;
import com.farecalculator.dao.loader.CappedFareDataLoader;
import com.farecalculator.dao.loader.PeakOffPeakFareDataLoader;
import com.farecalculator.dao.loader.PeakTimeDataLoader;
import com.farecalculator.exception.ApplicationException;
import com.farecalculator.model.Journey;
import com.farecalculator.processors.*;
import com.farecalculator.service.FareCalculatorService;
import com.farecalculator.service.FareController;
import com.farecalculator.utils.FileUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FareCalculatorTest {

  @Before
  public void setup() {
    CacheManager cacheManager = CacheManager.getInstance();
    PeakOffPeakFareCache peakOffPeakFareCache =
        new PeakOffPeakFareCache(new PeakOffPeakFareDataLoader());
    peakOffPeakFareCache.createDataMap();
    cacheManager.registerCache(peakOffPeakFareCache);

    CappedFareCache cappedFareCache = new CappedFareCache(new CappedFareDataLoader());
    cappedFareCache.createDataMap();
    cacheManager.registerCache(cappedFareCache);

    PeakTimeCache peakTimeCache = new PeakTimeCache(new PeakTimeDataLoader());
    peakTimeCache.createDataMap();
    cacheManager.registerCache(peakTimeCache);
  }

  @Test
  public void testPeakOffPeakFareRule() {
    RuleProcessor peakOffPeakFareRule = new PeakOffPeakFareRule(null);
    List<Journey> journeys = new ArrayList<>();
    addJourneyTestData(journeys);
    journeys.add(new Journey.JourneyBuilder("01-04-2022 19:00 1 2").build()); // 35
    Context context = new Context();
    Assert.assertEquals(155.00, peakOffPeakFareRule.process(journeys, context), 0);
  }

  private void addJourneyTestData(List<Journey> journeys) {
    journeys.add(new Journey.JourneyBuilder("01-04-2022 10:20 1 1").build()); // 30
    journeys.add(new Journey.JourneyBuilder("01-04-2022 10:25 2 1").build()); // 35
    journeys.add(new Journey.JourneyBuilder("01-04-2022 18:15 1 1").build()); // 30
    journeys.add(new Journey.JourneyBuilder("01-04-2022 16:15 1 1").build()); // 25
  }

  @Test
  public void testWhenDailyCapFareRuleApplied() {
    RuleProcessor peakOffPeakFareRule = new PeakOffPeakFareRule(new DailyCapFareRule(null));
    List<Journey> journeys = new ArrayList<>();
    addJourneyTestData(journeys);
    journeys.add(new Journey.JourneyBuilder("01-04-2022 19:00 1 2").build()); // 35
    Context context = new Context();
    double fare = peakOffPeakFareRule.process(journeys, context);
    Assert.assertEquals(120.00, fare, 0);
    Assert.assertEquals(5, journeys.size(), 0);
    Assert.assertTrue(journeys.stream().anyMatch(journey -> journey.getFare() == 0.0));
  }

  @Test
  public void testDailyCapFareRuleWhenNoInput() {
    RuleProcessor peakOffPeakFareRule = new DailyCapFareRule(null);
    List<Journey> journeys = new ArrayList<>();
    Context context = new Context();
    double fare = peakOffPeakFareRule.process(journeys, context);
    Assert.assertEquals(0.0, fare, 0);
  }

  @Test
  public void testWeeklyCapFareRuleWhenNoInput() {
    RuleProcessor peakOffPeakFareRule = new WeeklyCapFareRule(null);
    List<Journey> journeys = new ArrayList<>();
    Context context = new Context();
    double fare = peakOffPeakFareRule.process(journeys, context);
    Assert.assertEquals(0.0, fare, 0);
  }

  @Test
  public void testDailyCapFareRuleNotApplied() {
    RuleProcessor peakOffPeakFareRule = new PeakOffPeakFareRule(new DailyCapFareRule(null));
    List<Journey> journeys = new ArrayList<>();
    journeys.add(new Journey.JourneyBuilder("01-04-2022 10:20 1 1").build()); // 30
    journeys.add(new Journey.JourneyBuilder("01-04-2022 10:25 2 1").build()); // 35
    journeys.add(new Journey.JourneyBuilder("01-04-2022 18:15 1 1").build()); // 30
    Context context = new Context();
    double fare = peakOffPeakFareRule.process(journeys, context);
    Assert.assertEquals(95.00, fare, 0);
    Assert.assertEquals(3, journeys.size(), 0);
  }

  @Test
  public void testDailyCapFareRuleWithMultiDays() {
    RuleProcessor peakOffPeakFareRule = new PeakOffPeakFareRule(new DailyCapFareRule(null));
    List<Journey> journeys = new ArrayList<>();
    addJourneyTestData(journeys);
    journeys.add(new Journey.JourneyBuilder("01-05-2022 10:00 1 1").build()); // 30
    Context context = new Context();
    double fare = peakOffPeakFareRule.process(journeys, context);
    Assert.assertEquals(150.00, fare, 0);
    Assert.assertEquals(5, journeys.size(), 0);
    Assert.assertTrue(journeys.stream().noneMatch(journey -> journey.getFare() == 0.0));
  }

  @Test
  public void testWeeklyFareRule() throws IOException, ApplicationException {
    RuleProcessor peakOffPeakFareRule =
        new PeakOffPeakFareRule(new DailyCapFareRule(new WeeklyCapFareRule(null)));
    FileUtil fileUtil = new FileUtil();
    File inputFile = fileUtil.getFile("weekly_journey.txt");
    List<String> journeys = fileUtil.readFile(inputFile);

    FareCalculatorService service = new FareCalculatorService(peakOffPeakFareRule);
    FareController controller = new FareController(service);
    double fare = controller.getFare(journeys);
    Assert.assertEquals(600, fare, 0);
    Assert.assertEquals(26, journeys.size(), 0);
  }

  @Test
  public void testWeeklyFareRuleWhenMultiWeekly() throws IOException, ApplicationException {
    RuleProcessor peakOffPeakFareRule =
        new PeakOffPeakFareRule(new DailyCapFareRule(new WeeklyCapFareRule(null)));
    FileUtil fileUtil = new FileUtil();
    File inputFile = fileUtil.getFile("multi_weekly_journey.txt");
    List<String> journeys = fileUtil.readFile(inputFile);

    FareCalculatorService service = new FareCalculatorService(peakOffPeakFareRule);
    FareController controller = new FareController(service);
    double fare = controller.getFare(journeys);
    Assert.assertEquals(665, fare, 0);
    Assert.assertEquals(28, journeys.size(), 0);
  }
}
