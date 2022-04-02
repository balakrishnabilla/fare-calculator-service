package com.farecalulator.service;

import com.farecalulator.model.Journey;
import com.farecalulator.processors.Context;
import com.farecalulator.processors.RuleProcessor;

import java.util.List;

public class FareCalculatorService {
  RuleProcessor chainProcessors = null;

  public FareCalculatorService(RuleProcessor chainProcessors) {
    this.chainProcessors = chainProcessors;
  }

  public double calculate(List<Journey> journeyList) {
    Context context = new Context();
    return chainProcessors.process(journeyList, context);
  }
}
