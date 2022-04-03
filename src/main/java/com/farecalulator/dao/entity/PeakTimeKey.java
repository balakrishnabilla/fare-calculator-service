package com.farecalulator.dao.entity;

import com.farecalulator.dao.DataKey;

import java.time.DayOfWeek;
import java.util.Objects;

public class PeakTimeKey implements DataKey {
  private DayOfWeek dayOfWeek;

  public PeakTimeKey(DayOfWeek dayOfWeek) {
    this.dayOfWeek = dayOfWeek;
  }

  public DayOfWeek getDayOfWeek() {
    return dayOfWeek;
  }

  public void setDayOfWeek(DayOfWeek dayOfWeek) {
    this.dayOfWeek = dayOfWeek;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    PeakTimeKey that = (PeakTimeKey) o;
    return dayOfWeek == that.dayOfWeek;
  }

  @Override
  public int hashCode() {
    return Objects.hash(dayOfWeek);
  }
}
