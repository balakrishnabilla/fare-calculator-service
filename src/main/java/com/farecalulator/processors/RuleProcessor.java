package com.farecalulator.processors;

import com.farecalulator.model.Journey;

import java.util.List;

public interface RuleProcessor {
    double process(List<Journey> journeyList, Context context);
}
