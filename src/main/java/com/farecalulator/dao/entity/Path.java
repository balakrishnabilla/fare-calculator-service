package com.farecalulator.dao.entity;

import com.farecalulator.dao.DataKey;

import java.util.Objects;

public class Path implements DataKey {
  private int fromZone;
  private int toZone;

  public Path(int from, int to) {
    this.fromZone = from;
    this.toZone = to;
  }

  public int getFromZone() {
    return fromZone;
  }

  public int getToZone() {
    return toZone;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Path path = (Path) o;
    return fromZone == path.fromZone && toZone == path.toZone;
  }

  @Override
  public int hashCode() {
    return Objects.hash(fromZone, toZone);
  }

  @Override
  public String toString() {
    return "Path{" + "fromZone=" + fromZone + ", toZone=" + toZone + '}';
  }
}
