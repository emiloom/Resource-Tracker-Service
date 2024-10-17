package com.restinginbed.TeamProject;

import java.io.Serial;
import java.io.Serializable;

import jakarta.persistence.*;

/**
 * Represents a User that is browsing the website
 */
@Entity
@Table(name = "users")
public class User implements Serializable {

  @Serial
  private static final long serialVersionUID = 123456L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "longitude")
  private double longitude;

  @Column(name = "latitude")
  private double latitude;

  /**
   * Constructs a User using the parameters
   * Initial longitude and latitude is set to 0
   *
   * @param id      unique id associated with User
   */
  public User(int id) {
    this.id = id;
    this.longitude = 0;
    this.latitude = 0;
  }

  /**
   * Constructs a User using the parameters
   *
   * @param id          unique id associated with User
   * @param longitude   the longitude of the User
   * @param latitude    the latitude of the User
   */
  public User(int id, double longitude, double latitude) {
    this.id = id;
    this.longitude = longitude;
    this.latitude = latitude;
  }

  public int getId() { return id; }
  public void setId(int id) { this.id = id; }

  public double getLongitude() { return longitude; }
  public void setLongitude(double longitude) { this.longitude = longitude; }

  public double getLatitude() { return latitude; }
  public void setLatitude(double latitude) { this.latitude = latitude; }
}