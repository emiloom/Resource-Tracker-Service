package com.restinginbed.TeamProject;

public class LocationQueryDTO {
  private int id;
  private String locationQuery;

  public LocationQueryDTO() {}

  public LocationQueryDTO(int id, String locationQuery) {
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
