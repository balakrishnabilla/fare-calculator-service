package com.farecalulator.processors;

import com.farecalulator.model.Journey;

import java.util.List;

public abstract class AbstractFareRuleProcessor implements RuleProcessor {
  private RuleProcessor nextProcessor;

  public AbstractFareRuleProcessor(RuleProcessor nextProcessor) {
    this.nextProcessor = nextProcessor;
  }

  @Override
  public double process(List<Journey> journeyList, Context context) {
    if (nextProcessor != null) {
      return nextProcessor.process(journeyList, context);
    }
    return 0.0;
  }

  protected boolean hasNextChainAvailable() {
    return nextProcessor != null;
  }
}
