package com.farecalculator.dao.entity;

import com.farecalculator.dao.Data;

public class CappedFareData implements Data {
  private int fromZone;
  private int toZone;
  private double dailyCap;
  private double weeklyCap;

  public CappedFareData(int fromZone, int toZone, double dailyCap, double weeklyCap) {
    this.fromZone = fromZone;
    this.toZone = toZone;
    this.dailyCap = dailyCap;
    this.weeklyCap = weeklyCap;
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

  public double getDailyCap() {
    return dailyCap;
  }

  public void setDailyCap(double dailyCap) {
    this.dailyCap = dailyCap;
  }

  public double getWeeklyCap() {
    return weeklyCap;
  }

  public void setWeeklyCap(double weeklyCap) {
    this.weeklyCap = weeklyCap;
  }
}
