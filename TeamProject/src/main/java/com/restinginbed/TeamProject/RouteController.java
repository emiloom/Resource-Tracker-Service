package com.restinginbed.TeamProject;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;

/**
 * This class contains all the API routes for the system.
 */
@RestController
@RequestMapping("/api")
public class RouteController {

  private HashMap<String, Item> items = new HashMap<>();
  private HashMap<String, User> users = new HashMap<>();
  private HashMap<String, Organization> organizations = new HashMap<>();

  // Create an Item
  @PostMapping("/createItem")
  public ResponseEntity<String> createItem(@RequestBody Item item) {
    items.put(item.getId(), item);
    return new ResponseEntity<>("Item created successfully", HttpStatus.CREATED);
  }

  // Create a User
  @PostMapping("/createUser")
  public ResponseEntity<String> createUser(@RequestBody User user) {
    users.put(user.getId(), user);
    return new ResponseEntity<>("User created successfully", HttpStatus.CREATED);
  }

  // Create an Organization
  @PostMapping("/createOrganization")
  public ResponseEntity<String> createOrganization(@RequestBody Organization organization) {
    organizations.put(organization.getId(), organization);
    return new ResponseEntity<>("Organization created successfully", HttpStatus.CREATED);
  }

  // Update an Item
  @PatchMapping("/updateItem")
  public ResponseEntity<String> updateItem(@RequestBody Item item) {
    if (items.containsKey(item.getId())) {
      items.put(item.getId(), item);
      return new ResponseEntity<>("Item updated successfully", HttpStatus.OK);
    } else {
      return new ResponseEntity<>("Item not found", HttpStatus.NOT_FOUND);
    }
  }

  // Update a User
  @PatchMapping("/updateUser")
  public ResponseEntity<String> updateUser(@RequestBody User user) {
    if (users.containsKey(user.getId())) {
      users.put(user.getId(), user);
      return new ResponseEntity<>("User updated successfully", HttpStatus.OK);
    } else {
      return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
    }
  }

  // Update an Organization
  @PatchMapping("/updateOrganization")
  public ResponseEntity<String> updateOrganization(@RequestBody Organization organization) {
    if (organizations.containsKey(organization.getId())) {
      organizations.put(organization.getId(), organization);
      return new ResponseEntity<>("Organization updated successfully", HttpStatus.OK);
    } else {
      return new ResponseEntity<>("Organization not found", HttpStatus.NOT_FOUND);
    }
  }

  // Delete an Item
  @DeleteMapping("/deleteItem")
  public ResponseEntity<String> deleteItem(@RequestParam String itemId) {
    if (items.containsKey(itemId)) {
      items.remove(itemId);
      return new ResponseEntity<>("Item deleted successfully", HttpStatus.OK);
    } else {
      return new ResponseEntity<>("Item not found", HttpStatus.NOT_FOUND);
    }
  }

}

