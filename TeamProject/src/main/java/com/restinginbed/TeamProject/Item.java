package com.restinginbed.TeamProject;

import java.io.Serial;
import java.io.Serializable;

/**
 * Represents an item that is stored in an organization.
 * This class stores information about the item including its id, name, count
 * and which organization location it is stored in.
 */
public class Item implements Serializable {

  /**
   * Constructs a new Item object with the given parameters.
   * Initial count starts at 0 and initial description is empty
   *
   * @param id            unique id associated with item
   * @param name          the name of the item
   * @param organization  the organization this item is located in
   */
  public Item(int id, String name, String organization) {
    this.id = id;
    this.count = 0;
    this.description = "";
    this.organization = organization;
  }

  /**
   * Constructs a new Item object with the given parameters.
   * Initial description is empty.
   *
   * @param id            unique id associated with item
   * @param name          the name of the item
   * @param count         the count of the item
   * @param organization  the organization this item is located in
   */
  public Item(int id, String name, int count, String organization) {
    this.id = id;
    this.count = count;
    this.description = "";
    this.organization = organization;
  }

  /**
   * Constructs a new Item object with the given parameters.
   *
   * @param id            the unique id associated with this item
   * @param name          the name of the item
   * @param description   the description of the item
   * @param count         the count of the item
   * @param organization  the organization this item is located in
   */
  public Item(int id, String name, String description, int count, String organization) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.count = count;
    this.organization = organization;
  }

  public int getId() { return id; }
  public void setId(int id) { this.id = id; }

  public String getName() { return name; }
  public void setName(String name) { this.name = name; }

  public String getDescription() { return description; }
  public void setDescription(String description) { this.description = description; }

  public int getCount() { return count; }
  public void setCount(int count) { this.count = count; }

  public String getOrganization() { return organization; }
  public void setOrganization(String organization) { this.organization = organization; }

  /**
   * Adds num to count of items.
   *
   * @param num   num to add to count of items
   * @return      returns true if successful
   */
  public boolean addCount(int num) {
    this.count += num;
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
    if (this.count >= num) {
      this.count -= num;
      return true;
    }
    return false;
  }



  @Serial
  private static final long serialVersionUID = 234567L;
  private int id;
  private String name;
  private String description;
  private int count;
  private String organization;

}