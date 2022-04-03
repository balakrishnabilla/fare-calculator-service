package com.farecalulator.service;

import com.farecalulator.exception.ApplicationException;
import com.farecalulator.model.Journey;
import com.farecalulator.processors.PeakOffPeakFareRule;
import com.farecalulator.validator.Validator;

import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class FareController {
  private static final Logger LOGGER = Logger.getLogger(PeakOffPeakFareRule.class.getName());
  FareCalculatorService fareCalculatorService = null;
  Validator<String> validator = null;

  public FareController(FareCalculatorService fareCalculatorService) {
    this.fareCalculatorService = fareCalculatorService;
  }

  public void setValidator(Validator validator) {
    this.validator = validator;
  }

  public double getFare(List<String> journeyList) throws ApplicationException {
    validate(journeyList);
    List<Journey> sortedJourneyList =
        parse(journeyList).stream().sorted().collect(Collectors.toList());
    return fareCalculatorService.calculate(sortedJourneyList);
  }

  private void validate(List<String> journeyList) throws ApplicationException {
    if (this.validator == null) {
      LOGGER.info("validator is not setup, so hence alidations");
      return;
    }
    for (String journey : journeyList) {
      this.validator.validate(journey);
    }
  }

  private List<Journey> parse(List<String> journeyList) {
    return journeyList.stream()
        .filter(j -> !j.isEmpty())
        .map(journey -> new Journey.JourneyBuilder(journey).build())
        .collect(Collectors.toList());
  }
}
