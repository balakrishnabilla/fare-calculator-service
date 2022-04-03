package com.farecalulator.utils;

import com.farecalulator.model.PeakSchedule;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PeakUtil {

  private static Map<DayOfWeek, List<PeakSchedule>> peakHourMap = new HashMap<>();

  static {
    loadPeakHourMap();
  }

  public static void loadPeakHourMap() {
    loadWeekDayPeakHour();
    loadWeekEndPeakHour();
  }

  private static void loadWeekDayPeakHour() {
    List<PeakSchedule> weekDaySchedules = new ArrayList<>();
    PeakSchedule morning = new PeakSchedule(LocalTime.of(7, 0), LocalTime.of(10, 30));
    PeakSchedule evening = new PeakSchedule(LocalTime.of(17, 0), LocalTime.of(20, 0));
    weekDaySchedules.add(morning);
    weekDaySchedules.add(evening);
    peakHourMap.put(DayOfWeek.MONDAY, weekDaySchedules);
    peakHourMap.put(DayOfWeek.TUESDAY, weekDaySchedules);
    peakHourMap.put(DayOfWeek.WEDNESDAY, weekDaySchedules);
    peakHourMap.put(DayOfWeek.THURSDAY, weekDaySchedules);
    peakHourMap.put(DayOfWeek.FRIDAY, weekDaySchedules);
  }

  private static void loadWeekEndPeakHour() {
    List<PeakSchedule> weekEndSchedules = new ArrayList<>();
    PeakSchedule morning = new PeakSchedule(LocalTime.of(9, 0), LocalTime.of(11, 0));
    PeakSchedule evening = new PeakSchedule(LocalTime.of(18, 0), LocalTime.of(22, 0));
    weekEndSchedules.add(morning);
    weekEndSchedules.add(evening);
    peakHourMap.put(DayOfWeek.SATURDAY, weekEndSchedules);
    peakHourMap.put(DayOfWeek.SUNDAY, weekEndSchedules);
  }

  public static boolean isPeakHour(DayOfWeek dayOfWeek, LocalTime time) {
    return peakHourMap.get(dayOfWeek).stream()
        .anyMatch(
            peakSchedule ->
                time.compareTo(peakSchedule.getStartTime()) >= 0
                    && time.compareTo(peakSchedule.getEndTime()) <= 0);
  }
}
