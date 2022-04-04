package com.farecalculator.service;

import com.farecalculator.model.Journey;
import com.farecalculator.processors.Context;
import com.farecalculator.processors.RuleProcessor;

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
