package com.farecalculator.processors;

import com.farecalculator.model.Journey;

import java.util.List;

public abstract class AbstractFareRuleProcessor implements RuleProcessor {
  private RuleProcessor nextProcessor;

  protected AbstractFareRuleProcessor(RuleProcessor nextProcessor) {
    this.nextProcessor = nextProcessor;
  }

  @Override
  public double process(List<Journey> journeyList, Context context) {
    if (nextProcessor != null) {
      return nextProcessor.process(journeyList, context);
    }
    return journeyList.stream().mapToDouble(Journey::getFare).sum();
  }

  protected boolean hasNextChainAvailable() {
    return nextProcessor != null;
  }
}
