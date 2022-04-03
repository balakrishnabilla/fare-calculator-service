package com.farecalulator.utils;

import com.farecalulator.dao.entity.Path;
import com.farecalulator.model.PeakSchedule;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FareUtil {

  private static Map<DayOfWeek, List<PeakSchedule>> peakHourMap = new HashMap<>();

  private static Map<Path, Double> peakHourFareMap = new HashMap<>();
  private static Map<Path, Double> offPeakHourFareMap = new HashMap<>();

  private static Map<Path, Double> dailyCapMap = new HashMap<>();
  private static Map<Path, Double> weeklyCapMap = new HashMap<>();

  static {
    loadFare();
    loadDailyFareCap();
    loadWeeklyFareCap();
    loadPeakHourMap();
  }

  public static Double getPeakHourFare(Path path) {
    return peakHourFareMap.get(path);
  }

  public static Double getOffPeakHourFare(Path path) {
    return offPeakHourFareMap.get(path);
  }

  public static double getDailyCap(Path path) {
    return dailyCapMap.get(path);
  }

  public static Double getWeeklyCap(Path path) {
    return weeklyCapMap.get(path);
  }

  public static void loadDailyFareCap() {
    dailyCapMap.put(new Path(1, 1), 100.00);
    dailyCapMap.put(new Path(1, 2), 120.00);
    dailyCapMap.put(new Path(2, 1), 120.00);
    dailyCapMap.put(new Path(2, 2), 80.00);
  }

  public static void loadWeeklyFareCap() {
    weeklyCapMap.put(new Path(1, 1), 500.00);
    weeklyCapMap.put(new Path(1, 2), 600.00);
    weeklyCapMap.put(new Path(2, 1), 600.00);
    weeklyCapMap.put(new Path(2, 2), 400.00);
  }

  public static void loadPeakHourMap() {
    loadWeekDayPeakHour();
    loadWeekEndPeakHour();
  }

  public static void loadFare() {

    loadPeakFare();
    loadOffPeakFare();
  }

  private static void loadOffPeakFare() {
    offPeakHourFareMap.put(new Path(1, 1), 25.00);
    offPeakHourFareMap.put(new Path(1, 2), 30.00);
    offPeakHourFareMap.put(new Path(2, 1), 30.00);
    offPeakHourFareMap.put(new Path(2, 2), 20.00);
  }

  private static void loadPeakFare() {
    peakHourFareMap.put(new Path(1, 1), 30.00);
    peakHourFareMap.put(new Path(1, 2), 35.00);
    peakHourFareMap.put(new Path(2, 1), 35.00);
    peakHourFareMap.put(new Path(2, 2), 25.00);
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
