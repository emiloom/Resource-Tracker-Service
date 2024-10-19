package com.restinginbed.teamproject;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.restinginbed.teamproject.jparepositories.ClientRepository;
import com.restinginbed.teamproject.jparepositories.ItemRepository;
import com.restinginbed.teamproject.jparepositories.OrganizationRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * This class contains all the API routes for the system.
 */
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
  private ClientRepository clientRepository;

  @Autowired
  private ItemRepository itemRepository;

  @Autowired
  private GooglePlacesService googlePlacesService;

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
   * Create a new client in the database.
   */
  @PostMapping(value = "/createClient", consumes = MediaType.APPLICATION_JSON_VALUE,
          produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> createClient(@RequestBody Client client) {
    try {
      Client savedClient = clientRepository.save(client);
      return new ResponseEntity<>(savedClient, HttpStatus.CREATED);
    } catch (Exception e) {
      return handleException(e);
    }
  }

  /**
   * Create a new item in the database.
   */
  @PostMapping(value = "/createItem", consumes = MediaType.APPLICATION_JSON_VALUE,
          produces = MediaType.APPLICATION_JSON_VALUE)
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
   *
   * @param organization the organization to create
   * @return ResponseEntity containing the created organization and HTTP status
   */
  @PostMapping(value = "/createOrganization", consumes = MediaType.APPLICATION_JSON_VALUE,
          produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> createOrganization(@RequestBody Organization organization) {
    try {
      Organization savedOrganization = organizationRepository.save(organization);
      return new ResponseEntity<>(savedOrganization, HttpStatus.CREATED);
    } catch (Exception e) {
      // Handle the exception appropriately
      return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * Resolves the geographic coordinates (latitude and longitude) for a given location query.
   *
   * @param locationQueryDataTransferObject   the DTO containing the location query string
   * @return                                  a {@link ResponseEntity} containing a
   *                                          {@link LocationResponseDataTransferObject} with
   *                                          the resolved latitude and longitude, or an error
   *                                          response in case of failure
   */
  @PostMapping(value = "/resolveLocation", consumes = MediaType.APPLICATION_JSON_VALUE,
          produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> resolveLocation(
          @RequestBody LocationQueryDataTransferObject locationQueryDataTransferObject) {
    try {
      String placeDetails = googlePlacesService.getPlaceDetails(
              locationQueryDataTransferObject.getLocationQuery());

      ObjectMapper objectMapper = new ObjectMapper();
      JsonNode jsonNode = objectMapper.readTree(placeDetails);
      double longitude = jsonNode.path("geometry").path("location").path("lng").asDouble();
      double latitude = jsonNode.path("geometry").path("location").path("lat").asDouble();

      LocationResponseDataTransferObject locationResponse = new LocationResponseDataTransferObject(
              latitude, longitude);
      return new ResponseEntity<>(locationResponse, HttpStatus.OK);
    } catch (Exception e) {
      return handleException(e);
    }
  }

  //  /**
  //   * Create a new organization in the database using raw SQL.
  //   *
  //   * @param organization the organization to create
  //   * @return ResponseEntity containing HTTP status
  //   */
  //  @PostMapping(value = "/createOrganizationNative", consumes = MediaType.APPLICATION_JSON_VALUE,
  //  produces = MediaType.APPLICATION_JSON_VALUE)
  //  public ResponseEntity<?> createOrganizationNative(@RequestBody Organization organization) {
  //    try {
  //      // Call the native query method to insert with the given ID
  //      organizationRepository.insertOrganization(organization.getId(), organization.getName(),
  //      organization.getLocation());
  //      return new ResponseEntity<>(HttpStatus.CREATED);
  //    } catch (Exception e) {
  //      // Log the error and return an error response
  //      e.printStackTrace();
  //      return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
  //    }
  //  }

  /**
   * Returns the details of the specified client.
   *
   * @param clientId A {@code Integer} representing the client.
   *
   * @return A {@code ResponseEntity} object containing either the details of the Client and
   *         an HTTP 200 response or, an appropriate message indicating the proper response.
   */
  @GetMapping(value = "/retrieveClient", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> retrieveClient(@RequestParam(value = "clientId") Integer clientId) {
    try {
      Optional<Client> client = clientRepository.findById(clientId); // Use clientID directly
      return client.map(value -> new ResponseEntity<Object>(value, HttpStatus.OK))
              .orElse(new ResponseEntity<Object>("Client not found", HttpStatus.NOT_FOUND));
    } catch (Exception e) {
      return handleException(e);
    }
  }

  /**
   * Returns the details of the specified item.
   *
   * @param itemId A {@code Integer} representing the item.
   *
   * @return A {@code ResponseEntity} object containing either the details of the Item and
   *         an HTTP 200 response or, an appropriate message indicating the proper response.
   */
  @GetMapping(value = "/retrieveItem", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> retrieveItem(@RequestParam(value = "itemId") Integer itemId) {
    try {
      Optional<Item> item = itemRepository.findById(itemId);
      return item.map(value -> new ResponseEntity<Object>(value, HttpStatus.OK))
              .orElse(new ResponseEntity<Object>("Item not found", HttpStatus.NOT_FOUND));
    } catch (Exception e) {
      return handleException(e);
    }
  }

  /**
   * Returns the details of the specified organization.
   *
   * @param organizationId A {@code Integer} representing the organization.
   *
   * @return A {@code ResponseEntity} object containing either the details of the Organization and
   *         an HTTP 200 response or, an appropriate message indicating the proper response.
   */
  @GetMapping(value = "/retrieveOrganization", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> retrieveOrganization(
          @RequestParam(value = "organizationId") Integer organizationId) {
    try {
      Optional<Organization> organization = organizationRepository.findById(organizationId);
      return organization.map(value -> new ResponseEntity<Object>(value, HttpStatus.OK))
              .orElse(new ResponseEntity<Object>("Organization not found", HttpStatus.NOT_FOUND));
    } catch (Exception e) {
      return handleException(e);
    }
  }

  /**
   * Updates an existing client in the database.
   *
   * @param clientId A {@code Integer} representing the client ID.
   * @param client A {@code Client} object containing updated client details.
   * @return A {@code ResponseEntity} object containing the updated Client or an error message.
   */
  @PatchMapping(value = "/updateClient/{clientId}", consumes = MediaType.APPLICATION_JSON_VALUE,
          produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> updateClient(@PathVariable Integer clientId,
                                        @RequestBody Client client) {
    try {
      if (!clientRepository.existsById(clientId.intValue())) {
        return new ResponseEntity<>("Client not found", HttpStatus.NOT_FOUND);
      }

      Client existingClient = clientRepository.findById(clientId.intValue())
              .orElseThrow(() -> new IllegalArgumentException("Client not found"));

      existingClient.setName(client.getName());
      existingClient.setLocation(client.getLocation());

      Client updatedClient = clientRepository.save(existingClient);

      return new ResponseEntity<>(updatedClient, HttpStatus.OK);
    } catch (Exception e) {
      return handleException(e);
    }
  }

  /**
   * Deletes a client from the database.
   *
   * @param clientId A {@code Integer} representing the client ID.
   * @return A {@code ResponseEntity} indicating the result of the deletion.
   */
  @DeleteMapping(value = "/deleteClient/{clientId}")
  public ResponseEntity<?> deleteClient(@PathVariable Integer clientId) {
    try {
      if (!clientRepository.existsById(clientId.intValue())) {
        return new ResponseEntity<>("Client not found", HttpStatus.NOT_FOUND);
      }
      clientRepository.deleteById(clientId.intValue());
      return new ResponseEntity<>("Client deleted successfully", HttpStatus.OK);
    } catch (Exception e) {
      return handleException(e);
    }
  }

  /**
   * Updates an existing item in the database.
   *
   * @param itemId A {@code Integer} representing the item ID.
   * @param item A {@code Item} object containing updated item details.
   * @return A {@code ResponseEntity} object containing the updated Item or an error message.
   */
  @PatchMapping(value = "/updateItem/{itemId}", consumes = MediaType.APPLICATION_JSON_VALUE,
          produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> updateItem(@PathVariable Integer itemId, @RequestBody Item item) {
    try {
      if (!itemRepository.existsById(itemId)) {
        return new ResponseEntity<>("Item not found", HttpStatus.NOT_FOUND);
      }
      item.setId(itemId);
      Item updatedItem = itemRepository.save(item);
      return new ResponseEntity<>(updatedItem, HttpStatus.OK);
    } catch (Exception e) {
      return handleException(e);
    }
  }

  /**
   * Deletes an item from the database.
   *
   * @param itemId A {@code Integer} representing the item ID.
   * @return A {@code ResponseEntity} indicating the result of the deletion.
   */
  @DeleteMapping(value = "/deleteItem/{itemId}")
  public ResponseEntity<?> deleteItem(@PathVariable Integer itemId) {
    try {
      if (!itemRepository.existsById(itemId)) {
        return new ResponseEntity<>("Item not found", HttpStatus.NOT_FOUND);
      }
      itemRepository.deleteById(itemId);
      return new ResponseEntity<>("Item deleted successfully", HttpStatus.OK);
    } catch (Exception e) {
      return handleException(e);
    }
  }

  /**
   * Updates an existing organization in the database.
   *
   * @param organizationId  A {@code Integer} representing the organization ID.
   * @param organization    A {@code Organization} object containing updated organization details.
   * @return                A {@code ResponseEntity} object containing the updated
   *                        Organization or an error message.
   */
  @PatchMapping(value = "/updateOrganization/{organizationId}",
          consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> updateOrganization(@PathVariable Integer organizationId,
                                              @RequestBody Organization organization) {
    try {
      if (!organizationRepository.existsById(organizationId)) {
        return new ResponseEntity<>("Organization not found", HttpStatus.NOT_FOUND);
      }
      Organization updatedOrganization = organizationRepository.save(organization);
      return new ResponseEntity<>(updatedOrganization, HttpStatus.OK);
    } catch (Exception e) {
      return handleException(e);
    }
  }

  /**
   * Deletes an organization from the database.
   *
   * @param organizationId A {@code Integer} representing the organization ID.
   * @return A {@code ResponseEntity} indicating the result of the deletion.
   */
  @DeleteMapping(value = "/deleteOrganization/{organizationId}")
  public ResponseEntity<?> deleteOrganization(@PathVariable Integer organizationId) {
    try {
      if (!organizationRepository.existsById(organizationId)) {
        return new ResponseEntity<>("Organization not found", HttpStatus.NOT_FOUND);
      }
      organizationRepository.deleteById(organizationId);
      return new ResponseEntity<>("Organization deleted successfully", HttpStatus.OK);
    } catch (Exception e) {
      return handleException(e);
    }
  }

  /**
   * Retrieves all clients from the database.
   *
   * @return            a {@link ResponseEntity} containing a list of all clients
   *                    with an HTTP status of OK (200), or an error response in case of failure
   */
  @GetMapping(value = "/clients", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> getAllClients() {
    try {
      List<Client> clients = clientRepository.findAll();
      return new ResponseEntity<>(clients, HttpStatus.OK);
    } catch (Exception e) {
      return handleException(e);
    }
  }

  /**
   * Retrieves all organizations from the database.
   *
   * @return             a {@link ResponseEntity} containing a list of all
   *                     organizations with an HTTP status of OK (200), or an error response
   *                     in case of failure
   */
  @GetMapping(value = "/organizations", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> getAllOrganizations() {
    try {
      List<Organization> organizations = organizationRepository.findAll();
      return new ResponseEntity<>(organizations, HttpStatus.OK);
    } catch (Exception e) {
      return handleException(e);
    }
  }

  /**
   * Retrieves a List of items owned by and organization by matching their organizationIds
   * The organizationId field for the Items table is a foreign key from the organizations table.
   *
   * @param organizationId      A {@code Integer} representing the organization ID.
   * @return                    ResponseEntity with the List of items found
   */
  @GetMapping(value = "/organizations/{organizationId}/items",
          produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> getOrganizationItems(@PathVariable Integer organizationId) {
    try {
      Optional<List<Item>> items = Optional.of(itemRepository.findByOrganizationId(organizationId));
      if (items.isPresent()) {
        return new ResponseEntity<>(items.get(), HttpStatus.OK);
      } else {
        return new ResponseEntity<>("No items found", HttpStatus.NOT_FOUND);
      }
    } catch (Exception e) {
      return handleException(e);
    }
  }

  /**
   * Searches for items by name in the database.
   *
   * @param name        the name or partial name of the item(s) to search for
   * @return            a {@link ResponseEntity} containing the list of matching items with an HTTP
   *                    status of OK (200), or a message indicating no items were found with a
   *                    status of NOT FOUND (404), or an error response in case of failure
   * @see ItemRepository
   */
  @GetMapping(value = "/searchItems", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> searchItems(@RequestParam String name) {
    try {
      Optional<List<Item>> items = Optional.of(itemRepository.findByNameContaining(name));
      if (items.isPresent()) {
        return new ResponseEntity<>(items.get(), HttpStatus.OK);
      } else {
        return new ResponseEntity<>("No items found", HttpStatus.NOT_FOUND);
      }
    } catch (Exception e) {
      return handleException(e);
    }
  }

  private ResponseEntity<?> handleException(Exception e) {
    System.out.println(e.toString());
    return new ResponseEntity<>("An error has occurred", HttpStatus.INTERNAL_SERVER_ERROR);
  }

}
