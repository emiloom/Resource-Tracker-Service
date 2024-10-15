package com.restinginbed.TeamProject;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

/**
 * This is a class representing an organization object. In this project, we define organization as
 * an entity that is providing a resource for people. They will own a list of resources (items) that they 
 * have to track the availability of.
 */
public class Organization implements Serializable{ 

  /**
     * Constructs an Organization with the specified details.
     *
     * @param id          the unique identifier for the organization
     * @param name        the name of the organization
     * @param description a description of the organization
     * @param longitude   the longitude of the organization's location
     * @param latitude    the latitude of the organization's location
     */
    public Organization(int id, String name, String description, double longitude, double latitude) {
      this.id = id;
      this.name = name;
      this.description = description;
      this.longitude = longitude;
      this.latitude = latitude;
  }

  /**
   * Constructs an Organization with the specified details, 
   * defaulting location to (0, 0).
   *
   * @param id          the unique identifier for the organization
   * @param name        the name of the organization
   * @param description a description of the organization
   */
  public Organization(int id, String name, String description) {
      this.id = id;
      this.name = name;
      this.description = description;
      this.longitude = 0;
      this.latitude = 0;
  }

  /**
   * Adds an Item to the organization.
   *
   * @param item the Item to be added
   */
  public boolean addItem(Item item) {
      items.add(item);
      return true;
      // log success + transaction?
  }

  /**
   * Deletes an Item from the organization.
   *
   * @param item the Item to be deleted
   */
  public boolean deleteItem(Item item) {
      items.remove(item);
      return true;
  }

  /**
   * Retrieves an Item by its unique identifier.
   *
   * @param id the unique identifier of the Item
   * @return the Item with the specified id
   * @throws IllegalArgumentException if the Item is not found
   */
  public Item getItem(int id) throws IllegalArgumentException {
      for (Item item : items) {
          if (item.getId() == id) {
              return item;
          }
      }
      throw new IllegalArgumentException("Item not found");
  }

  /**
   * Sets the longitude of the organization's location.
   *
   * @param longitude the longitude to set
   */
  public void setLongitude(double longitude) {
      this.longitude = longitude;
  }

  /**
   * Sets the latitude of the organization's location.
   *
   * @param latitude the latitude to set
   */
  public void setLatitude(double latitude) {
      this.latitude = latitude;
  }

  /**
   * Changes the name of the organization.
   *
   * @param name the new name for the organization
   */
  public void changeName(String name) {
      this.name = name;
  }

  /**
   * Updates the description of the organization.
   *
   * @param description the new description for the organization
   */
  public void updateDescription(String description) {
      this.description = description;
  }

  /**
   * Gets the unique identifier of the organization.
   *
   * @return the id of the organization
   */
  public int getId() {
      return id;
  }

  /**
   * Gets the name of the organization.
   *
   * @return the name of the organization
   */
  public String getName() {
      return name;
  }

  /**
   * Gets the description of the organization.
   *
   * @return the description of the organization
   */
  public String getDescription() {
      return description;
  }

  /**
   * Gets the location of the organization as an array of 
   * [longitude, latitude].
   *
   * @return an array containing the longitude and latitude
   */
  public double[] getLocation() {
      return new double[]{longitude, latitude};
  }

  /**
   * Gets the list of Items associated with the organization.
   *
   * @return a list of Items
   */
  public List<Item> getItems() {
      return items;
  }
  
  @Serial
  private static final long serialVersionUID = 234567L;
  private final int id;
  private String name;
  private String description;
  private double longitude;
  private double latitude;
  private List<Item> items = new ArrayList<>();

}
