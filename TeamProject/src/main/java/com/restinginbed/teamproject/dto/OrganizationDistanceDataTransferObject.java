package com.restinginbed.teamproject.dto;

import com.restinginbed.teamproject.model.Organization;

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

  public Organization getOrganization() {
    return organization;
  }

  public void setOrganization(Organization organization) {
    this.organization = organization;
  }

  public double getDistance() {
    return distance;
  }

  public void setDistance(double distance) {
    this.distance = distance;
  }
}

