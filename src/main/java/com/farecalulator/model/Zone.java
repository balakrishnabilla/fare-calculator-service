package com.farecalulator.model;

public enum Zone {
  ZONE_ONE(1),
  ZONE_TWO(2);
  private int zoneNum;

  Zone(int zoneNum) {
    this.zoneNum = zoneNum;
  }

  public static Zone of(int zoneNum) {
    switch (zoneNum) {
      case 1:
        return ZONE_ONE;
      case 2:
        return ZONE_TWO;
      default:
    }
    throw new IllegalArgumentException("Zone - '" + zoneNum + "' is not valid");
  }
}
