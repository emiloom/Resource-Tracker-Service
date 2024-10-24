package com.restinginbed.teamproject;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serial;
import java.io.Serializable;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Represents a User that is browsing the website.
 */
@Entity
@Table(name = "client")
public class Client implements Serializable {

  @Serial
  private static final long serialVersionUID = 123456L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "client_id")
  private int clientId;

  @Column(name = "clientName")
  private String clientName;

  @Column(name = "clientLocation")
  private String clientLocation;

  @Autowired
  private transient GooglePlacesService googlePlacesService;

  /**
   * Constructs a Client using the parameters.
   * Initial location set to blank string
   *
   * @param name   name associated with Client
   */
  public Client(String name) {
    this.clientName = name;
    this.clientLocation = "";
  }

  /**
   * Constructs a Client using the parameters.
   *
   * @param name          name associated with Client
   * @param location      location associated with Client
   */
  public Client(String name, String location) {
    this.clientName = name;
    setLocationFromQuery(location);
  }

  // no-argument constructor for JPA
  public Client() {
    this.clientLocation = "";
  }

  public String getName() {
    return this.clientName;
  }

  public Integer getId() {
    return this.clientId;
  }

  public void setName(String name) {
    this.clientName = name;
  }

  public String getLocation() {
    return this.clientLocation;
  }

  public void setLocation(String location) {
    this.clientLocation = location;
  }

  /**
   * Retrieves the longitude from the client's location string.
   *
   * @return the longitude as a {@code double}
   * @throws IllegalArgumentException if the {@code clientLocation} is null,
   *                                  empty, or not in the correct format, or if the
   *                                  longitude value cannot be parsed as a double.
   */
  public double getLongitude() {
    if (this.clientLocation != null && !this.clientLocation.isEmpty()) {
      String[] coordinates = this.clientLocation.split(",\\s*");
      if (coordinates.length == 2) {
        try {
          return Double.parseDouble(coordinates[1].trim());
        } catch (NumberFormatException e) {
          throw new IllegalArgumentException("Invalid longitude value in clientLocation");
        }
      }
    }
    throw new IllegalArgumentException("Invalid clientLocation format");
  }

  /**
   * Retrieves the latitude from the client's location string.
   *
   * @return                          longitude as a {@code double}
   * @throws IllegalArgumentException if the {@code clientLocation} is null,
   *                                  empty, or not in the correct format, or if the
   *                                  longitude value cannot be parsed as a double.
   */
  public double getLatitude() {
    if (this.clientLocation != null && !this.clientLocation.isEmpty()) {
      String[] coordinates = this.clientLocation.split(",\\s*");
      if (coordinates.length == 2) {
        try {
          return Double.parseDouble(coordinates[0].trim());
        } catch (NumberFormatException e) {
          throw new IllegalArgumentException("Invalid latitude value in clientLocation");
        }
      }
    }
    throw new IllegalArgumentException("Invalid clientLocation format");
  }


  /**
   * Sets the client's location using a query string to find coordinates.
   *
   * @param query the search query to find the location
   */
  public void setLocationFromQuery(String query) {
    String coordinates = googlePlacesService.getPlaceCoordinates(query);
    if (coordinates.startsWith("Latitude")) {
      this.clientLocation = coordinates.replace("Latitude: ", "").replace("Longitude: ", "");
    } else {
      throw new IllegalArgumentException("Could not find location for the given query.");
    }
  }
}
