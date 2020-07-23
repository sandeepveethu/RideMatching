package com.seeker.ridematching.domain;

public class RideDetails {
  private String riderName;
  private String driverName;
  private int riderRating;
  private int driverRating;

  public String getRiderName() {
    return riderName;
  }

  public void setRiderName(String riderName) {
    this.riderName = riderName;
  }

  public String getDriverName() {
    return driverName;
  }

  public void setDriverName(String driverName) {
    this.driverName = driverName;
  }

  public int getRiderRating() {
    return riderRating;
  }

  public void setRiderRating(int riderRating) {
    this.riderRating = riderRating;
  }

  public int getDriverRating() {
    return driverRating;
  }

  public void setDriverRating(int driverRating) {
    this.driverRating = driverRating;
  }
}
