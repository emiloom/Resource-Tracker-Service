package com.restinginbed.teamproject;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.io.Serial;
import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;

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

  @Autowired
  private transient GooglePlacesService googlePlacesService;

  /**
   * Default constructor required by JPA.
   */
  public Organization() {
  }

  /**
   * Constructs an Organization with the specified name and location.
   *
   * @param name     the name of the organization
   * @param location the location of the organization
   */
  public Organization(String name, String location) {
    this.name = name;
    setLocationFromQuery(location);
  }

  /**
   * Constructs an Organization with the specified name, defaulting location to an empty string.
   *
   * @param name the name of the organization
   */
  public Organization(String name) {
    this.name = name;
    this.location = "";
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
  public boolean setName(String name) {
    this.name = name;
    return true;
  }

  /**
   * Gets the location of the organization.
   *
   * @return the organization's location
   */
  public String getLocation() {
    return location;
  }

  /**
   * Sets the location of the organization.
   *
   * @param location the new location of the organization
   */
  public boolean setLocation(String location) {
    this.location = location;
    return true;
  }

  /**
   * Retrieves the longitude from the organization's location string.
   *
   * @return the longitude as a {@code double}
   * @throws IllegalArgumentException if the {@code location} is null,
   *                                  empty, or not in the correct format, or if the
   *                                  longitude value cannot be parsed as a double.
   */
  public double getLongitude() {
    if (this.location != null && !this.location.isEmpty()) {
      String[] coordinates = this.location.split(",\\s*");
      if (coordinates.length == 2) {
        try {
          return Double.parseDouble(coordinates[1].trim());
        } catch (NumberFormatException e) {
          throw new IllegalArgumentException("Invalid longitude value in location");
        }
      }
    }
    throw new IllegalArgumentException("Invalid location format");
  }

  /**
   * Retrieves the latitude from the organization's location string.
   *
   * @return the latitude as a {@code double}
   * @throws IllegalArgumentException if the {@code location} is null,
   *                                  empty, or not in the correct format, or if the
   *                                  latitude value cannot be parsed as a double.
   */
  public double getLatitude() {
    if (this.location != null && !this.location.isEmpty()) {
      String[] coordinates = this.location.split(",\\s*");
      if (coordinates.length == 2) {
        try {
          return Double.parseDouble(coordinates[0].trim());
        } catch (NumberFormatException e) {
          throw new IllegalArgumentException("Invalid latitude value in location");
        }
      }
    }
    throw new IllegalArgumentException("Invalid location format");
  }

  /**
   * Sets the client's location using a query string to find coordinates.
   *
   * @param query the search query to find the location
   */
  public void setLocationFromQuery(String query) {
    String coordinates = googlePlacesService.getPlaceCoordinates(query);
    if (coordinates.startsWith("Latitude")) {
      this.location = coordinates.replace("Latitude: ", "").replace("Longitude: ", "");
    } else {
      throw new IllegalArgumentException("Could not find location for the given query.");
    }
  }

}
