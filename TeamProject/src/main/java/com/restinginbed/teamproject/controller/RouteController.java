package com.restinginbed.teamproject.controller;



import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.restinginbed.teamproject.dto.LocationQueryDataTransferObject;
import com.restinginbed.teamproject.dto.LocationResponseDataTransferObject;
import com.restinginbed.teamproject.dto.OrganizationDistanceDataTransferObject;
import com.restinginbed.teamproject.model.Client;
import com.restinginbed.teamproject.model.Item;
import com.restinginbed.teamproject.model.Organization;
import com.restinginbed.teamproject.repository.ClientRepository;
import com.restinginbed.teamproject.repository.ItemRepository;
import com.restinginbed.teamproject.repository.OrganizationRepository;
import com.restinginbed.teamproject.service.ClientService;
import com.restinginbed.teamproject.service.GooglePlacesService;
import com.restinginbed.teamproject.service.OrganizationService;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
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

  @Autowired
  private OrganizationService organizationService;

  @Autowired
  private ClientService clientService;

  public static final Logger logger = Logger.getLogger(RouteController.class.getName());

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
      clientService.updateOrganizationCoordinates(client);
      Client savedClient = clientRepository.save(client);
      return new ResponseEntity<>(savedClient, HttpStatus.CREATED);
    } catch (IllegalArgumentException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
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
    } catch (IllegalArgumentException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
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
      organizationService.updateOrganizationCoordinates(organization);
      Organization savedOrganization = organizationRepository.save(organization);
      return new ResponseEntity<>(savedOrganization, HttpStatus.CREATED);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * Resolves the latitude and longitude for a given location query.
   *
   * @param locationQueryDataTransferObject the request body containing the location query string.
   * @return a {@link ResponseEntity} with the resolved latitude and longitude in a
   *         {@link LocationResponseDataTransferObject}, or an error response if resolution fails.
   */
  @PostMapping(value = "/resolveLocation", consumes = MediaType.APPLICATION_JSON_VALUE,
          produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> resolveLocation(@RequestBody LocationQueryDataTransferObject
                                                     locationQueryDataTransferObject) {
    try {
      String placeDetails = googlePlacesService.getPlaceDetails(locationQueryDataTransferObject
              .getLocationQuery());

      ObjectMapper objectMapper = new ObjectMapper();
      JsonNode jsonNode = objectMapper.readTree(placeDetails);
      double longitude = jsonNode.path("geometry").path("location").path("lng").asDouble();
      double latitude = jsonNode.path("geometry").path("location").path("lat").asDouble();

      LocationResponseDataTransferObject locationResponse = new LocationResponseDataTransferObject(
              latitude, longitude);
      return new ResponseEntity<>(locationResponse, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * Resolves the distance between two entities: either two clients, two organizations,
   * or one client and one organization.
   *
   * @param originId The id of the origin entity
   * @param originType The type of the origin entity ("client" or "organization").
   * @param destId The ID of the destination entity.
   * @param destType The type of the destination entity ("client" or "organization").
   * @return ResponseEntity with the distance between the two entities or an error response.
   */
  @GetMapping(value = "/resolveDistance", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> resolveDistance(
      @RequestParam(value = "originId") Integer originId,
      @RequestParam(value = "originType") String originType,
      @RequestParam(value = "destId") Integer destId,
      @RequestParam(value = "destType") String destType) {
    try {
      double originLat;
      double originLng;
      double destLat;
      double destLng;

      // get coordinates for origin
      if (originType.equalsIgnoreCase("client")) {
        Optional<Client> originClient = clientRepository.findById(originId);
        if (originClient.isPresent()) {
          originLat = originClient.get().getLatitude();
          originLng = originClient.get().getLongitude();
        } else {
          return new ResponseEntity<>("Origin client not found", HttpStatus.NOT_FOUND);
        }
      } else if (originType.equalsIgnoreCase("organization")) {
        Optional<Organization> originOrganization = organizationRepository.findById(originId);
        if (originOrganization.isPresent()) {
          originLat = originOrganization.get().getLatitude();
          originLng = originOrganization.get().getLongitude();
        } else {
          return new ResponseEntity<>("Origin organization not found", HttpStatus.NOT_FOUND);
        }
      } else {
        return new ResponseEntity<>("Invalid origin type", HttpStatus.BAD_REQUEST);
      }

      // Validate coordinates for origin
      if (!isValidCoordinates(originLat, originLng)) {
        return new ResponseEntity<>("Invalid location format", HttpStatus.BAD_REQUEST);
      }

      // get coordinates for the destination
      if (destType.equalsIgnoreCase("client")) {
        Optional<Client> destClient = clientRepository.findById(destId);
        if (destClient.isPresent()) {
          destLat = destClient.get().getLatitude();
          destLng = destClient.get().getLongitude();
        } else {
          return new ResponseEntity<>("Destination client not found", HttpStatus.NOT_FOUND);
        }
      } else if (destType.equalsIgnoreCase("organization")) {
        Optional<Organization> destOrganization = organizationRepository.findById(destId);
        if (destOrganization.isPresent()) {
          destLat = destOrganization.get().getLatitude();
          destLng = destOrganization.get().getLongitude();
        } else {
          return new ResponseEntity<>("Destination organization not found", HttpStatus.NOT_FOUND);
        }
      } else {
        return new ResponseEntity<>("Invalid destination type", HttpStatus.BAD_REQUEST);
      }

      // Validate coordinates for destination
      if (!isValidCoordinates(destLat, destLng)) {
        return new ResponseEntity<>("Invalid location format", HttpStatus.BAD_REQUEST);
      }

      // calculate the distance
      double distanceResponse = googlePlacesService.getDistanceBetweenLocations(
          originLat, originLng, destLat, destLng);

      return new ResponseEntity<>(distanceResponse, HttpStatus.OK);
    } catch (Exception e) {
      return handleException(e);
    }
  }

  private boolean isValidCoordinates(double lat, double lng) {
    return (lat >= -90 && lat <= 90) && (lng >= -180 && lng <= 180);
  }

  /**
   * Handles the ranking of organizations based on their proximity to a given origin 
   *  (either a client or an organization). The method calculates the distance between 
   *   the origin and all organizations in the database, sorts them by distance,
   *   and returns the sorted list of organizations along with their calculated distances.
   *
   * @param originId ID of the origin (client or organization)
   *
   * @param originType type of the origin, which can be either "client" or "organization"
   * 
   * @return a ResponseEntity containing the ranked list of organizations along with their 
   *     distances from the origin.
   * 
   * @throws Exception if any unexpected errors occur during the processing, 
   *     such as database errors or failed API calls.
   * 
   */
  @GetMapping(value = "/rankNearestOrganizations", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> rankNearestOrganizations(
        @RequestParam(value = "originId") Integer originId,
        @RequestParam(value = "originType") String originType) {
    try {
      // Log received parameters
      logger.info("Received originId: " + originId);
      logger.info("Received originType: " + originType);
      
      // Get origin coordinates
      double originLat;
      double originLng;
      if (originType.equalsIgnoreCase("client")) {
        Optional<Client> originClient = clientRepository.findById(originId);
        if (originClient.isEmpty()) {
          return new ResponseEntity<>("Origin client not found", HttpStatus.NOT_FOUND);
        }
        originLat = originClient.get().getLatitude();
        originLng = originClient.get().getLongitude();
      } else if (originType.equalsIgnoreCase("organization")) {
        logger.info("Processing as organization...");
        Optional<Organization> originOrganization = organizationRepository.findById(originId);
        if (originOrganization.isEmpty()) {
          logger.warning("Organization not found for ID: " + originId);
          return new ResponseEntity<>("Origin organization not found", HttpStatus.NOT_FOUND);
        }
        originLat = originOrganization.get().getLatitude();
        originLng = originOrganization.get().getLongitude();
        logger.info("Organization found: Lat=" + originLat + ", Lng=" + originLng);
      } else {
        return new ResponseEntity<>("Invalid origin type", HttpStatus.BAD_REQUEST);
      }

      // Retrieve all organizations
      List<Organization> organizations = organizationRepository.findAll();
      organizations.removeIf(org -> {
        try {
          org.getLatitude();
          org.getLongitude();
          return false; // Valid location, do not remove
        } catch (IllegalArgumentException e) {
          logger.warning("Invalid location format for organization ID: " + org.getOrganizationId());
          return true; // Remove invalid organization
        }
      });

      // Calculate distances using GooglePlacesService
      List<OrganizationDistanceDataTransferObject> rankedDistances = new ArrayList<>();
      for (Organization organization : organizations) {
        try {
          double destLat = organization.getLatitude();
          double destLng = organization.getLongitude();
          
          // Call GooglePlacesService to calculate distance
          double distanceResponse = googlePlacesService.getDistanceBetweenLocations(
                  originLat, originLng, destLat, destLng);

          // Add to ranked list
          rankedDistances.add(
              new OrganizationDistanceDataTransferObject(organization, distanceResponse));
        } catch (Exception e) {
          logger.warning("Failed to calculate distance for organization ID: " 
                  + organization.getOrganizationId() 
                  + ". Error: " + e.getMessage());
        }
      }

      // Sort by distance
      rankedDistances.sort(
          Comparator.comparingDouble(OrganizationDistanceDataTransferObject::getDistance));

      return new ResponseEntity<>(rankedDistances, HttpStatus.OK);
    } catch (Exception e) {
      return handleException(e);
    }
  }


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
      Optional<Client> client = clientRepository.findById(clientId); // Use clientId directly
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
  public ResponseEntity<?> retrieveOrganization(@RequestParam(value = "organizationId")
                                                  Integer organizationId) {
    try {
      Optional<Organization> organization = organizationRepository.findById(organizationId);
      return organization.map(value -> new ResponseEntity<Object>(value, HttpStatus.OK))
              .orElse(new ResponseEntity<Object>("Organization not found",
                      HttpStatus.NOT_FOUND));
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
      if (!clientRepository.existsById(clientId)) {
        return new ResponseEntity<>("Client not found", HttpStatus.NOT_FOUND);
      }

      Client existingClient = clientRepository.findById(clientId)
              .orElseThrow(() -> new IllegalArgumentException("Client not found"));

      existingClient.setName(client.getName());
      existingClient.setLocation(client.getLocation());

      Client updatedClient = clientRepository.save(existingClient);

      logger.info("Client updated: ID" + clientId + "has been updated.");

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
      logger.info("ClientId " + clientId + " has been updated");
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
      logger.info("Item " + itemId + " has been updated");
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
      logger.info("Item " + itemId + " has been deleted");
      return new ResponseEntity<>("Item deleted successfully", HttpStatus.OK);
    } catch (Exception e) {
      return handleException(e);
    }
  }

  /**
   * Updates an existing organization in the database.
   *
   * @param organizationId A {@code Integer} representing the organization ID.
   * @param organization A {@code Organization} object containing updated organization details.
   * @return A {@code ResponseEntity} object containing the updated Organization
   *         or an error message.
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
      logger.info("Organization " + organizationId + " has been updated");
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
      logger.info("Organization " + organizationId + " has been deleted");
      return new ResponseEntity<>("Organization deleted successfully", HttpStatus.OK);
    } catch (Exception e) {
      return handleException(e);
    }
  }

  /**
   * Retrieves all clients from the database.
   *
   * @return A {@link ResponseEntity} containing a list of clients and HTTP status 200 (OK),
   *         or an error message if an exception occurs.
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
   * @return A {@link ResponseEntity} containing a list of organizations and HTTP status 200 (OK),
   *         or an error message if an exception occurs.
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
   * @param organizationId A {@code Integer} representing the organization ID.
   * @return ResponseEntity with the List of items found
   */
  @GetMapping(value = "/organizations/{organizationId}/items",
          produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> getOrganizationItems(@PathVariable Integer organizationId) {
    try {
      System.out.println(organizationId);
      List<Item> items = itemRepository.findByOrganizationId(organizationId);
      if (items != null && !items.isEmpty()) {
        return new ResponseEntity<>(items, HttpStatus.OK);
      } else {
        return new ResponseEntity<>("No items found", HttpStatus.NOT_FOUND);
      }
    } catch (Exception e) {
      return handleException(e);
    }
  }

  /**
   * Searches for items containing the specified search term.
   *
   * @param searchTerm A {@code String} representing the term to search for in item names.
   * @return A {@link ResponseEntity} containing a list of matching items if found,
   *         or an error message if not found.
   */
  @GetMapping(value = "/searchItems", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> searchItems(@RequestParam String searchTerm) {
    try {
      Optional<List<Item>> items = Optional.of(itemRepository.findByNameContaining(searchTerm));

      if (items.isPresent() && !items.get().isEmpty()) {
        return new ResponseEntity<>(items.get(), HttpStatus.OK);
      } else {
        return new ResponseEntity<>("No items found", HttpStatus.NOT_FOUND);
      }
    } catch (Exception e) {
      return handleException(e);
    }
  }

  /**
   * Handles exceptions that occur during API calls.
   *
   * @param e The {@link Exception} that was thrown.
   * @return A {@link ResponseEntity} indicating an internal server error has occurred.
   */
  private ResponseEntity<?> handleException(Exception e) {
    System.out.println(e.toString());
    return new ResponseEntity<>("An error has occurred", HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
