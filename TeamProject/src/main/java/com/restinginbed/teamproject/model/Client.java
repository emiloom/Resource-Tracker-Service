package com.restinginbed.teamproject.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serial;
import java.io.Serializable;
import java.util.logging.Logger;

/**
 * Represents a User that is browsing the website.
 */
@Entity
@Table(name = "client")
public class Client implements Serializable {

  @Serial
  private static final long serialVersionUID = 123456L;

  @Id
  @Column(name = "client_id", nullable = false)
  private int clientId;

  @Column(name = "clientName", nullable = false)
  private String clientName;

  @Column(name = "clientLocation", nullable = false)
  private String clientLocation;

  public static final Logger logger = Logger.getLogger(Client.class.getName());

  private double latitude;
  private double longitude;

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
    this.clientName = "";
    this.clientLocation = "";
  }

  public String getName() {
    return this.clientName;
  }

  public Integer getId() {
    return this.clientId;
  }

  public void setId(Integer newId) {
    this.clientId = newId;
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

  public double getLongitude() {
    return this.longitude;
  }

  public double getLatitude() {
    return this.latitude;
  }

  public void setLatitude(double latitude) {
    this.latitude = latitude;
  }

  public void setLongitude(double longitude) {
    this.longitude = longitude;
  }

}