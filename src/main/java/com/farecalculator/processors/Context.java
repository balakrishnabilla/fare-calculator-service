package com.farecalculator.processors;

import com.farecalculator.dao.entity.Path;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Context {
  private Map<LocalDate, Path> dailyFarthestPath = new HashMap<>();
  private Map<Integer, Path> weeklyFarthestPath = new HashMap<>();

  public Map<LocalDate, Path> getDailyFarthestPath() {
    return dailyFarthestPath;
  }

  public void setDailyFarthestPath(Map<LocalDate, Path> dailyFarthestPath) {
    this.dailyFarthestPath = dailyFarthestPath;
  }

  public Map<Integer, Path> getWeeklyFarthestPath() {
    return weeklyFarthestPath;
  }

  public void setWeeklyFarthestPath(Map<Integer, Path> weeklyFarthestPath) {
    this.weeklyFarthestPath = weeklyFarthestPath;
  }
}
