package com.farecalculator.service;

import com.farecalculator.exception.ApplicationException;
import com.farecalculator.model.Journey;
import com.farecalculator.validator.Validator;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class FareController {
  private static final Logger LOGGER = Logger.getLogger(FareController.class.getName());
  FareCalculatorService fareCalculatorService = null;
  Validator<String> validator = null;

  public FareController(FareCalculatorService fareCalculatorService) {
    this.fareCalculatorService = fareCalculatorService;
  }

  public void setValidator(Validator<String> validator) {
    this.validator = validator;
  }

  public double getFare(List<String> journeyList) throws ApplicationException {
    if (journeyList.isEmpty()) {
      return 0.0;
    }
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
