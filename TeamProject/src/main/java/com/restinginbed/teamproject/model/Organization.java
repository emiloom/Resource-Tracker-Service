package com.restinginbed.teamproject.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.io.Serial;
import java.io.Serializable;


/**
 * Represents an organization entity with an ID, name, and location.
 */
@Entity
public class Organization implements Serializable {

  @Serial
  private static final long serialVersionUID = 234567L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "organization_id", nullable = false)
  private int organizationId;

  @Column(name = "organization_name", nullable = false)
  private String name;

  @Column(name = "organization_location", nullable = false)
  private String location;

  private double latitude;
  private double longitude;

  /**
   * Default constructor required by JPA.
   */
  public Organization() {
    this.name = "";
    this.location = "";
  }

  /**
   * Constructs an Organization with the specified name and location.
   *
   * @param name     the name of the organization
   * @param location the location of the organization
   */
  public Organization(String name, String location) {
    this.name = name;
    this.location = location;
  }

  /**
   * Constructs an Organization with the specified name, defaulting location to an empty string.
   *
   * @param name the name of the organization
   */
  public Organization(String name) {
    this(name, "");
  }

  /**
   * Gets the unique identifier of the organization.
   *
   * @return the organization's ID
   */
  public int getOrganizationId() {
    return organizationId;
  }

  /**
   * Sets the id of the organization.
   *
   * @param id the new id of the organization.
   */
  public void setOrganizationId(int id) {
    this.organizationId = id;
  }

  /**
   * Gets the name of the organization.
   *
   * @return the organization's name
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name of the organization.
   *
   * @param name the new name of the organization
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Gets the location of the organization.
   *
   * @return the organization's location
   */
  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public double getLatitude() {
    return latitude;
  }

  public void setLatitude(double latitude) {
    this.latitude = latitude;
  }

  public double getLongitude() {
    return longitude;
  }

  public void setLongitude(double longitude) {
    this.longitude = longitude;
  }

}
