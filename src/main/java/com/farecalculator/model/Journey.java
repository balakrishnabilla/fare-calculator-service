package com.farecalculator.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

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

  public Journey(Journey journey) {
    this.time = LocalTime.of(journey.getTime().getHour(), journey.getTime().getMinute());
    this.date =
        LocalDate.of(
            journey.getDate().getYear(),
            journey.getDate().getMonth(),
            journey.getDate().getDayOfMonth());
    this.fare = journey.getFare();
    this.toZone = journey.getToZone();
    this.fromZone = journey.getFromZone();
  }

  @Override
  public int compareTo(Journey journey) {
    int diff = this.getDate().compareTo(journey.getDate());
    if (diff == 0) {
      return this.getTime().compareTo(journey.getTime());
    }
    return diff;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Journey journey = (Journey) o;
    return fromZone == journey.fromZone
        && toZone == journey.toZone
        && Objects.equals(date, journey.date)
        && Objects.equals(time, journey.time);
  }

  @Override
  public int hashCode() {
    return Objects.hash(date, time, fromZone, toZone, fare);
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
      String[] timePart = this.time.split(":");
      LocalTime localTime =
          LocalTime.of(Integer.parseInt(timePart[0]), Integer.parseInt(timePart[1]));
      return new Journey(localDate, localTime, fromZone, toZone);
    }
  }
}
