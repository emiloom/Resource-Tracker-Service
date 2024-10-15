package com.restinginbed.TeamProject;

import java.util.HashMap;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RouteController {

  /**
   * rn assuming a postgreSQL db, correct db configuration, 
   * Spring Data JPA to interact with db via repositories,
   * and entities to map classes to db tables.
   */

  @Autowired
  private OrganizationRepository organizationRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ItemRepository itemRepository;

  /**
   * Redirects to the homepage.
   *
   * @return A String containing the name of the html file to be loaded.
   */
  @GetMapping({"/", "/index", "/home"})
  public String index() {
    return "Welcome, in order to make an API call direct your browser or Postman to an endpoint "
        + "\n\n This can be done using the following format: \n\n http:127.0.0"
        + ".1:8080/endpoint?arg=value";
  }

    
  /**
   * Create a new user in the database.
   */
  @PostMapping(value = "/createUser", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> createUser(@RequestBody User user) {
    try {
      User savedUser = userRepository.save(user);
      return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    } catch (Exception e) {
      return handleException(e);
    }
  }

  /**
   * Create a new item in the database.
   */
  @PostMapping(value = "/createItem", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> createItem(@RequestBody Item item) {
    try {
      Item savedItem = itemRepository.save(item);
      return new ResponseEntity<>(savedItem, HttpStatus.CREATED);
    } catch (Exception e) {
      return handleException(e);
    }
  }

  /**
   * Create a new organization in the database.
   */
  @PostMapping(value = "/createOrganization", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> createOrganization(@RequestBody Organization organization) {
    try {
      Organization savedOrganization = organizationRepository.save(organization);
      return new ResponseEntity<>(savedOrganization, HttpStatus.CREATED);
    } catch (Exception e) {
      return handleException(e);
    }
  }

  /**
   * Returns the details of the specified user.
   *
   * @param userID A {@code Long} representing the user.
   *
   * @return A {@code ResponseEntity} object containing either the details of the User and
   *         an HTTP 200 response or, an appropriate message indicating the proper response.
   */
  @GetMapping(value = "/retrieveUser", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> retrieveUser(@RequestParam(value = "userID") Long userID) {
    try {
      Optional<User> user = userRepository.findById(userID);
      return user.map(value -> new ResponseEntity<Object>(value, HttpStatus.OK))
        .orElse(new ResponseEntity<Object>("User not found", HttpStatus.NOT_FOUND));
    } catch (Exception e) {
      return handleException(e);
    }
  }

  /**
   * Returns the details of the specified item.
   *
   * @param itemID A {@code Long} representing the item.
   *
   * @return A {@code ResponseEntity} object containing either the details of the Item and
   *         an HTTP 200 response or, an appropriate message indicating the proper response.
   */
  @GetMapping(value = "/retrieveItem", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> retrieveItem(@RequestParam(value = "itemID") Long itemID) {
    try {
      Optional<Item> item = itemRepository.findById(itemID);
      return item.map(value -> new ResponseEntity<Object>(value, HttpStatus.OK))
        .orElse(new ResponseEntity<Object>("Item not found", HttpStatus.NOT_FOUND));
    } catch (Exception e) {
      return handleException(e);
    }
  }

  /**
   * Returns the details of the specified organization.
   *
   * @param organizationID A {@code Long} representing the organization.
   *
   * @return A {@code ResponseEntity} object containing either the details of the Organization and
   *         an HTTP 200 response or, an appropriate message indicating the proper response.
   */
  @GetMapping(value = "/retrieveOrganization", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> retrieveOrganization(@RequestParam(value = "organizationID") Long organizationID) {
    try {
      Optional<Organization> organization = organizationRepository.findById(organizationID);
      return organization.map(value -> new ResponseEntity<Object>(value, HttpStatus.OK))
        .orElse(new ResponseEntity<Object>("Organization not found", HttpStatus.NOT_FOUND));
    } catch (Exception e) {
      return handleException(e);
    }
  }

  private ResponseEntity<?> handleException(Exception e) {
    System.out.println(e.toString());
    return new ResponseEntity<>("An error has occurred", HttpStatus.INTERNAL_SERVER_ERROR);
  }
  
}
