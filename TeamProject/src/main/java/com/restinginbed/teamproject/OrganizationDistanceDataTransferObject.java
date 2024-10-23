package com.restinginbed.teamproject;

/**
 * A Data Transfer Object (DTO) representing the calculated distance of an organization
 * from the origin.
 */
public class OrganizationDistanceDataTransferObject {
  private Organization organization;
  private double distance;

  public OrganizationDistanceDataTransferObject(Organization organization, double distance) {
    this.organization = organization;
    this.distance = distance;
  }

  public Organization geOrganization(){
    return organization;
  }

  public void setOrganization(Organization organization) {
    this.organization = organization;
  }

  public double getDistance(){
    return distance;
  }

  public void setDistance(double distance) {
    this.distance = distance;
  }
}

