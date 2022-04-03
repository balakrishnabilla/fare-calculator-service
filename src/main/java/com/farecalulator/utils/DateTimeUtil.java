package com.farecalulator.utils;

import java.time.LocalDate;
import java.util.Calendar;

public class DateTimeUtil {
  private DateTimeUtil() {}

  public static int getWeekNumber(LocalDate localDate) {
    Calendar calendar = Calendar.getInstance();
    calendar.setFirstDayOfWeek(Calendar.MONDAY);
    calendar.set(localDate.getYear(), localDate.getMonthValue() - 1, localDate.getDayOfMonth());
    return calendar.get(Calendar.WEEK_OF_YEAR);
  }
}
