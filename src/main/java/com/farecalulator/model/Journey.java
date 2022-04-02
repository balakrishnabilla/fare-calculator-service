package com.farecalulator.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Journey implements Comparable<Journey> {
  private LocalDate date;
  private LocalTime time;
  private int fromZone;
  private int toZone;
  private Double fare;

  public Journey() {}

  private Journey(LocalDate date, LocalTime time, int fromZone, int toZone) {
    this.date = date;
    this.time = time;
    this.fromZone = fromZone;
    this.toZone = toZone;
  }

  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  public LocalTime getTime() {
    return time;
  }

  public int getFromZone() {
    return fromZone;
  }

  public void setFromZone(int fromZone) {
    this.fromZone = fromZone;
  }

  public int getToZone() {
    return toZone;
  }

  public void setToZone(int toZone) {
    this.toZone = toZone;
  }

  public Double getFare() {
    return fare;
  }

  public void setFare(Double fare) {
    this.fare = fare;
  }

  public Journey clone() {
    Journey journey = new Journey();
    journey.setFare(this.getFare());
    journey.setToZone(this.getToZone());
    journey.setDate(this.getDate());
    journey.setFromZone(this.getFromZone());
    return journey;
  }

  @Override
  public int compareTo(Journey journey) {
    int date = this.getDate().compareTo(journey.getDate());
    if (date == 0) {
      return this.getTime().compareTo(journey.getTime());
    }
    return date;
  }

  @Override
  public String toString() {
    return "Journey{"
        + "date="
        + date
        + ", time="
        + time
        + ", FromZone="
        + fromZone
        + ", ToZone="
        + toZone
        + ", fare="
        + fare
        + '}';
  }

  public static class JourneyBuilder {
    public final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private String date;
    private String time;
    private int fromZone;
    private int toZone;
    private double fare;

    public JourneyBuilder(String journey) {
      journey = journey.trim();
      String[] data = journey.split(" ");
      this.date = data[0];
      this.time = data[1];
      this.fromZone = Integer.parseInt(data[2]);
      this.toZone = Integer.parseInt(data[3]);
    }

    public Journey build() {
      LocalDate localDate = LocalDate.parse(this.date, formatter);
      String[] time = this.time.split(":");
      LocalTime localTime = LocalTime.of(Integer.parseInt(time[0]), Integer.parseInt(time[1]));
      Journey journey = new Journey(localDate, localTime, fromZone, toZone);
      return journey;
    }

  }
}
