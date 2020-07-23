package com.seeker.ridematching.domain;

public class Driver {
  private String name;
  private double averageRating;
  private long rideCount;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public double getAverageRating() {
    return averageRating;
  }

  public void setAverageRating(double averageRating) {
    this.averageRating = averageRating;
  }

  public long getRideCount() {
    return rideCount;
  }

  public void setRideCount(long rideCount) {
    this.rideCount = rideCount;
  }
}
