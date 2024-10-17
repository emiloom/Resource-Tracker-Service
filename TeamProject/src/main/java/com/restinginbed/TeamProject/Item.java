package com.restinginbed.TeamProject;

import java.io.Serial;
import java.io.Serializable;

import jakarta.persistence.*;

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
  private int id;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "description", nullable = false)
  private String description;

  @Column(name = "count")
  private int count;

  @Column(name = "organizationId", nullable = false)
  private int organizationId;   //organization identified by its unique id rather than its name


  /**
   * Constructs a new Item object with the given parameters.
   * Initial count starts at 0 and initial description is empty
   *
   * @param id            unique id associated with item
   * @param name          the name of the item
   * @param organization  the organization this item is located in
   */
  public Item(int id, String name, int organization) {
    this.id = id;
    this.count = 0;
    this.description = "";
    this.organizationId = organization;
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
  public Item(int id, String name, int count, int organization) {
    this.id = id;
    this.count = count;
    this.description = "";
    this.organizationId = organization;
  }

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

  public int getId() { return id; }
  public void setId(int id) { this.id = id; }

  public String getName() { return name; }
  public void setName(String name) { this.name = name; }

  public String getDescription() { return description; }
  public void setDescription(String description) { this.description = description; }

  public int getCount() { return count; }
  public void setCount(int count) { this.count = count; }

  public int getOrganizationId() { return organizationId; }
  public void setOrganizationId(int organization) { this.organizationId = organization; }

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

}