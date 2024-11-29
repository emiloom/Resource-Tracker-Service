package com.restinginbed.teamproject.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serial;
import java.io.Serializable;
import java.util.logging.Logger;

/**
 * Represents an item that is stored in an organization.
 * This class stores information about the item including its id, name, count
 * and which organization location it is stored in.
 */
@Entity
@Table(name = "items")
public class Item implements Serializable {

  @Serial
  private static final long serialVersionUID = 234567L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "items_id")
  private int id;

  @Column(name = "items_name", nullable = false)
  private String name;

  @Column(name = "items_description", nullable = false)
  private String description;

  @Column(name = "quantity_available", nullable = false)
  private int count;

  @Column(name = "items_location", nullable = false)
  private String location;

  @Column(name = "organization_id", nullable = false)
  private int organizationId;   //organization identified by its unique id rather than its name

  public static final Logger logger = Logger.getLogger(Item.class.getName());

  /**
   * Constructs a new Item object with the given parameters.
   *
   * @param id            the unique id associated with this item
   * @param name          the name of the item
   * @param description   the description of the item
   * @param count         the count of the item
   * @param organizationId  the organization this item is located in
   */
  public Item(int id, String name, String description, int count, int organizationId) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.count = count;
    this.organizationId = organizationId;
  }

  // no-argument constructor for JPA
  public Item() {
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public int getCount() {
    return count;
  }

  /**
   * Sets new count of item.
   *
   * @param count     new count to be set to item
   */
  public void setCount(int count) {
    int oldCount = this.count;
    this.count = count;
    logger.info("Availability changed from " + oldCount + " to " + count);
  }

  public int getOrganizationId() {
    return organizationId;
  }

  public void setOrganizationId(int organization) {
    this.organizationId = organization;
  }

  /**
   * Adds num to count of items.
   *
   * @param num   num to add to count of items
   * @return      returns true if successful
   */
  public boolean addCount(int num) {
    int oldCount = this.count;
    this.count += num;
    logger.info("Availability changed from " + oldCount  + " to " + count);
    return true;
  }

  /**
   * Removes num from count of items.
   *
   * @param num     num to remove from count
   * @return        returns true if num was successfully removed from count.
   *                false if otherwise.
   */
  public boolean removeCount(int num) {
    int oldCount = this.count;
    if (oldCount >= num) {
      this.count -= num;
      logger.info("Availability changed from " + oldCount  + " to " + count);
      return true;
    }
    return false;
  }

}