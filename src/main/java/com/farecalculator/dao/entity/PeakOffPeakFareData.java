package com.farecalculator.dao.entity;

import com.farecalculator.dao.Data;

public class PeakOffPeakFareData implements Data {
  private int fromZone;
  private int toZone;
  private double peakFare;
  private double offPeakFare;

  public PeakOffPeakFareData(int fromZone, int toZone, double peakFare, double offPeakFare) {
    this.fromZone = fromZone;
    this.toZone = toZone;
    this.peakFare = peakFare;
    this.offPeakFare = offPeakFare;
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

  public double getPeakFare() {
    return peakFare;
  }

  public void setPeakFare(double peakFare) {
    this.peakFare = peakFare;
  }

  public double getOffPeakFare() {
    return offPeakFare;
  }

  public void setOffPeakFare(double offPeakFare) {
    this.offPeakFare = offPeakFare;
  }
}
