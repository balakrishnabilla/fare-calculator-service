package com.farecalculator;

import com.farecalculator.dao.*;
import com.farecalculator.dao.cache.CappedFareCache;
import com.farecalculator.dao.cache.PeakOffPeakFareCache;
import com.farecalculator.dao.cache.PeakTimeCache;
import com.farecalculator.dao.loader.CappedFareDataLoader;
import com.farecalculator.dao.loader.PeakOffPeakFareDataLoader;
import com.farecalculator.dao.loader.PeakTimeDataLoader;
import com.farecalculator.exception.ApplicationException;
import com.farecalculator.processors.DailyCapFareRule;
import com.farecalculator.processors.PeakOffPeakFareRule;
import com.farecalculator.processors.RuleProcessor;
import com.farecalculator.processors.WeeklyCapFareRule;
import com.farecalculator.service.FareCalculatorService;
import com.farecalculator.service.FareController;
import com.farecalculator.utils.FileUtil;
import com.farecalculator.validator.DefaultValidator;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class AppMain {

  private static final Logger LOGGER = Logger.getLogger(AppMain.class.getName());

  public static void main(String[] args) throws ApplicationException, IOException {

    PeakOffPeakFareCache peakOffPeakFareCache =
        new PeakOffPeakFareCache(new PeakOffPeakFareDataLoader());
    peakOffPeakFareCache.createDataMap();
    CacheManager cacheManager = CacheManager.getInstance();
    cacheManager.registerCache(peakOffPeakFareCache);

    CappedFareCache cappedFareCache = new CappedFareCache(new CappedFareDataLoader());
    cappedFareCache.createDataMap();
    cacheManager.registerCache(cappedFareCache);

    PeakTimeCache peakTimeCache = new PeakTimeCache(new PeakTimeDataLoader());
    peakTimeCache.createDataMap();
    cacheManager.registerCache(peakTimeCache);
    WeeklyCapFareRule weeklyCapFareProcessor = new WeeklyCapFareRule(null);
    DailyCapFareRule dailyCapFareProcessor = new DailyCapFareRule(weeklyCapFareProcessor);
    RuleProcessor chainProcessors = new PeakOffPeakFareRule(dailyCapFareProcessor);

    FileUtil fileUtil = new FileUtil();
    List<String> input = null;
    try {
      input = fileUtil.readFile(new File(args[0]));
    } catch (IOException ex) {
      throw new ApplicationException(ex);
    }
    FareCalculatorService service = new FareCalculatorService(chainProcessors);
    FareController fareController = new FareController(service);
    fareController.setValidator(new DefaultValidator());
    String fare = String.valueOf(fareController.getFare(input));
    LOGGER.info(fare);
  }
}
