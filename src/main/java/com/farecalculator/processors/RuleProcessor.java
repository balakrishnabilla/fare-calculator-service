package com.farecalculator.processors;

import com.farecalculator.model.Journey;

import java.util.List;

public interface RuleProcessor {
    double process(List<Journey> journeyList, Context context);
}
