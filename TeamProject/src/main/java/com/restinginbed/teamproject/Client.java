package com.restinginbed.teamproject;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serial;
import java.io.Serializable;


/**
 * Represents a Client that is browsing the website.
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

  @Column(name = "client_name")
  private String clientName;

  @Column(name = "client_location")
  private String clientLocation;

  /**
   * Constructs a Client using the parameters.
   * Initial location set to blank string.
   *
   * @param name   name associated with Client
   */
  public Client(String name) {
    this.clientName = name;
  }

  /**
   * Constructs a Client using the parameters.
   *
   * @param name          name associated with Client
   * @param location      location associated with Client
   */
  public Client(String name, String location) {
    this.clientName = name;
    this.clientLocation = location;
  }

  // no-argument constructor for JPA
  public Client() {
    this.clientLocation = "";
  }

  public String getName() {
    return this.clientName;
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
          throw new IllegalArgumentException("Invalid longitude value in client_location");
        }
      }
    }
    throw new IllegalArgumentException("Invalid client_location format");
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
          throw new IllegalArgumentException("Invalid latitude value in client_location");
        }
      }
    }
    throw new IllegalArgumentException("Invalid client_location format");
  }

  /**
   * Sets the longitude in the client's location string.
   *
   * @param longitude                 the new longitude to set
   * @throws IllegalArgumentException if the {@code clientLocation} is null,
   *                                  empty, or not in the correct format.
   */
  public void setLongitude(double longitude) {
    if (this.clientLocation != null && !this.clientLocation.isEmpty()) {
      String[] coordinates = this.clientLocation.split(",\\s*");
      if (coordinates.length == 2) {
        this.clientLocation = coordinates[0] + ", " + longitude;
      } else {
        throw new IllegalArgumentException("Invalid client_location format");
      }
    } else {
      this.clientLocation = "0.0, " + longitude;
    }
  }

  /**
   * Sets the latitude in the client's location string.
   *
   * @param latitude                  the new latitude to set
   * @throws IllegalArgumentException if the {@code clientLocation} is null,
   *                                  empty, or not in the correct format.
   */
  public void setLatitude(double latitude) {
    if (this.clientLocation != null && !this.clientLocation.isEmpty()) {
      String[] coordinates = this.clientLocation.split(",\\s*");
      if (coordinates.length == 2) {
        this.clientLocation = latitude + ", " + coordinates[1];
      } else {
        throw new IllegalArgumentException("Invalid client_location format");
      }
    } else {
      this.clientLocation = latitude + ", 0.0";
    }
  }
}
