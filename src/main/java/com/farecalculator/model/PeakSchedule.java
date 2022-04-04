package com.farecalculator.model;

import java.time.LocalTime;

public class PeakSchedule {

  private LocalTime startTime;
  private LocalTime endTime;

  public PeakSchedule(LocalTime startTime, LocalTime endTime) {
    this.startTime = startTime;
    this.endTime = endTime;
  }

  public LocalTime getStartTime() {
    return startTime;
  }

  public LocalTime getEndTime() {
    return endTime;
  }
}
