package com.farecalulator;

import com.farecalulator.dao.*;
import com.farecalulator.dao.cache.CappedFareCache;
import com.farecalulator.dao.cache.PeakOffPeakFareCache;
import com.farecalulator.dao.cache.PeakTimeCache;
import com.farecalulator.dao.loader.CappedFareDataLoader;
import com.farecalulator.dao.loader.PeakOffPeakFareDataLoader;
import com.farecalulator.dao.loader.PeakTimeDataLoader;
import com.farecalulator.exception.ApplicationException;
import com.farecalulator.processors.DailyCapFareRule;
import com.farecalulator.processors.PeakOffPeakFareRule;
import com.farecalulator.processors.RuleProcessor;
import com.farecalulator.processors.WeeklyCapFareRule;
import com.farecalulator.service.FareCalculatorService;
import com.farecalulator.service.FareController;
import com.farecalulator.utils.FileUtil;
import com.farecalulator.validator.DefaultValidator;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class App {
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
    File inputFile = fileUtil.getFile("daily_journey.txt");
    List<String> input = fileUtil.readFile(inputFile);

    FareCalculatorService service = new FareCalculatorService(chainProcessors);

    FareController fareController = new FareController(service);
    fareController.setValidator(new DefaultValidator());
    System.out.println(fareController.getFare(input));
  }
}
