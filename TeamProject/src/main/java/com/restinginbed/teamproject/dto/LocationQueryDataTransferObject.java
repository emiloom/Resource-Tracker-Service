package com.restinginbed.teamproject.dto;

/**
 * A Data Transfer Object (DTO) for representing a location query with an associated ID.
 */
public class LocationQueryDataTransferObject {
  private int id;
  private String locationQuery;

  public LocationQueryDataTransferObject() {}

  public LocationQueryDataTransferObject(int id, String locationQuery) {
    this.id = id;
    this.locationQuery = locationQuery;
  }

  public int getId() {
    return id;
  }

  public String getLocationQuery() {
    return locationQuery;
  }

  public void setLocationQuery(String locationQuery) {
    this.locationQuery = locationQuery;
  }
}