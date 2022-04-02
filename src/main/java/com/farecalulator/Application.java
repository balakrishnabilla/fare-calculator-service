package com.farecalulator;

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

public class Application {
  public static void main(String[] args) throws ApplicationException, IOException {
    WeeklyCapFareRule weeklyCapFareProcessor = new WeeklyCapFareRule(null);
    DailyCapFareRule dailyCapFareProcessor = new DailyCapFareRule(weeklyCapFareProcessor);
    RuleProcessor chainProcessors = new PeakOffPeakFareRule(dailyCapFareProcessor);

   FileUtil fileUtil = new FileUtil();
   File inputFile = fileUtil.getFile("test_daily.txt");
   List<String> input = fileUtil.readFile(inputFile);

    FareCalculatorService service = new FareCalculatorService(chainProcessors);

   FareController fareController = new FareController(service);
   fareController.setValidator(new DefaultValidator());
   System.out.println(fareController.getFare(input));
  }
}
