package com.restinginbed.teamproject;

/**
 * A Data Transfer Object (DTO) representing a response that includes
 * latitude and longitude coordinates.
 */
public class LocationResponseDataTransferObject {
  private double latitude;
  private double longitude;

  public LocationResponseDataTransferObject(double latitude, double longitude) {
    this.latitude = latitude;
    this.longitude = longitude;
  }

  public double getLatitude() {
    return latitude;
  }

  public double getLongitude() {
    return longitude;
  }

  public void setLatitude(double latitude) {
    this.latitude = latitude;
  }

  public void setLongitude(double longitude) {
    this.longitude = longitude;
  }

}
