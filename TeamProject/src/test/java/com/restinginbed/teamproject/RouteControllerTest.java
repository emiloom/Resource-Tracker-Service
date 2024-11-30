package com.restinginbed.teamproject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.restinginbed.teamproject.controller.RouteController;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;



/**
 * Unit Tests for RouteController Class.
 */
@SpringBootTest
public class RouteControllerTest {

  @InjectMocks
  private RouteController mockRouteController;

  @Mock
  private ClientRepository mockClientRepository;

  @Mock
  private ItemRepository mockItemRepository;

  @Mock
  private OrganizationRepository mockOrganizationRepository;

  @Mock
  private GooglePlacesService mockGooglePlacesService;

  @Mock
  private ClientService clientService;

  @Mock
  private OrganizationService organizationService;

  private Client defaultClient;
  private Item defaultItem;
  private Organization defaultOrganization;

  /**
   * Initializes the necessary test objects and mocks before each test case.
   */
  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    defaultClient = new Client("test client", "1,1");
    defaultItem = new Item(0, "name", "test", 10, 0);;
    defaultOrganization = new Organization("test organization", "Empire State Building");
    defaultOrganization.setOrganizationId(2);
    defaultOrganization.setLocation("Empire State Building");
  }


  @Test
  public void testRankNearestOrganizations_WithClientOrigin() {
    Integer clientId = defaultClient.getId();

    when(mockClientRepository.findById(clientId)).thenReturn(Optional.of(defaultClient));

    List<Organization> organizations = Arrays.asList(defaultOrganization);
    when(mockOrganizationRepository.findAll()).thenReturn(organizations);
    when(mockGooglePlacesService.getDistanceBetweenLocations(anyDouble(),
            anyDouble(), anyDouble(), anyDouble()))
            .thenReturn(10.0);  // Assuming distance is 10.0 km

    ResponseEntity<?> response = mockRouteController.rankNearestOrganizations(clientId, "client");

    assertEquals(HttpStatus.OK, response.getStatusCode());
    List<OrganizationDistanceDataTransferObject> rankedDistances = (List<
            OrganizationDistanceDataTransferObject>) response.getBody();
    assertNotNull(rankedDistances);
    assertEquals(1, rankedDistances.size());
    assertEquals(10.0, rankedDistances.get(0).getDistance());
  }

  @Test
  public void testRankNearestOrganizations_WithOrganizationOrigin() {
    Integer orgId = defaultOrganization.getOrganizationId();

    when(mockOrganizationRepository.findById(orgId))
            .thenReturn(Optional.of(defaultOrganization));
    List<Organization> organizations = Arrays.asList(defaultOrganization);
    when(mockOrganizationRepository.findAll()).thenReturn(organizations);
    when(mockGooglePlacesService.getDistanceBetweenLocations(anyDouble(),
            anyDouble(), anyDouble(), anyDouble()))
            .thenReturn(5.0);  // Assuming distance is 5.0 km

    ResponseEntity<?> response = mockRouteController.rankNearestOrganizations(
            orgId, "organization");

    assertEquals(HttpStatus.OK, response.getStatusCode());
    List<OrganizationDistanceDataTransferObject> rankedDistances = (List<
            OrganizationDistanceDataTransferObject>) response.getBody();
    assertNotNull(rankedDistances);
    assertEquals(1, rankedDistances.size());
    assertEquals(5.0, rankedDistances.get(0).getDistance());
  }

  @Test
  public void testRankNearestOrganizations_InvalidOriginType() {
    Integer originId = defaultClient.getId();

    ResponseEntity<?> response = mockRouteController.rankNearestOrganizations(originId,
            "invalidType");

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("Invalid origin type", response.getBody());
  }

  @Test
  public void testRankNearestOrganizations_OriginNotFound_Client() {
    Integer clientId = 999; // Non-existing client

    when(mockClientRepository.findById(clientId)).thenReturn(Optional.empty());

    ResponseEntity<?> response = mockRouteController.rankNearestOrganizations(clientId,
            "client");

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("Origin client not found", response.getBody());
  }

  @Test
  public void testRankNearestOrganizations_OriginNotFound_Organization() {
    Integer orgId = 999;

    when(mockOrganizationRepository.findById(orgId)).thenReturn(Optional.empty());

    ResponseEntity<?> response = mockRouteController.rankNearestOrganizations(orgId,
            "organization");

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("Origin organization not found", response.getBody());
  }

  @Test
  public void testRankNearestOrganizations_NoValidLocations() {
    Integer clientId = defaultClient.getId();

    when(mockClientRepository.findById(clientId))
            .thenReturn(Optional.of(defaultClient));
    Organization invalidOrg = new Organization("Invalid Organization", "No location");
    invalidOrg.setOrganizationId(3);
    List<Organization> organizations = Arrays.asList(invalidOrg);
    when(mockOrganizationRepository.findAll()).thenReturn(organizations);

    ResponseEntity<?> response = mockRouteController.rankNearestOrganizations(clientId, "client");

    assertEquals(HttpStatus.OK, response.getStatusCode());
    List<OrganizationDistanceDataTransferObject> rankedDistances = (List<
            OrganizationDistanceDataTransferObject>) response.getBody();
  }

  @Test
  public void testUpdateClient_Success() {
    Integer clientId = defaultClient.getId();
    Client updatedClient = new Client("Updated Client", "Cornell University");
    updatedClient.setLocation("Columbia University");

    when(mockClientRepository.existsById(clientId)).thenReturn(true);
    when(mockClientRepository.findById(clientId)).thenReturn(Optional.of(defaultClient));
    when(mockClientRepository.save(any(Client.class))).thenReturn(updatedClient);

    ResponseEntity<?> response = mockRouteController.updateClient(clientId, updatedClient);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(updatedClient.getName(), ((Client) response.getBody()).getName());
    assertEquals(updatedClient.getLocation(), ((Client) response.getBody()).getLocation());
    verify(mockClientRepository, times(1)).save(any(Client.class));
  }

  @Test
  public void testUpdateClient_ClientNotFound() {
    Integer clientId = 999;  // Non-existing clientId
    Client updatedClient = new Client("Updated Client", "Cornell University");

    when(mockClientRepository.existsById(clientId)).thenReturn(false);

    ResponseEntity<?> response = mockRouteController.updateClient(clientId, updatedClient);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("Client not found", response.getBody());
    verify(mockClientRepository, never()).save(any(Client.class));
  }

  @Test
  public void testUpdateItem_Success() {
    Integer itemId = defaultItem.getId();
    Item updatedItem = new Item(defaultItem.getId(), "Updated Client",
            "test", 10,
            defaultItem.getOrganizationId());

    when(mockItemRepository.existsById(itemId)).thenReturn(true);
    when(mockItemRepository.findById(itemId)).thenReturn(Optional.of(defaultItem));
    when(mockItemRepository.save(any(Item.class))).thenReturn(updatedItem);

    ResponseEntity<?> response = mockRouteController.updateItem(itemId, updatedItem);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(updatedItem.getName(), ((Item) response.getBody()).getName());
    verify(mockItemRepository, times(1)).save(any(Item.class));
  }

  @Test
  public void testSearchItems_Success() {
    String searchTerm = "Test";
    List<Item> itemList = List.of(new Item(0, "Test Item", "test",
            10, 0));

    when(mockItemRepository.findByNameContaining(searchTerm)).thenReturn(itemList);

    ResponseEntity<?> response = mockRouteController.searchItems(searchTerm);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(itemList, response.getBody());
  }

  @Test
  public void testSearchItems_ItemNotFound() {
    String searchTerm = "Test";

    when(mockItemRepository.findByNameContaining(searchTerm)).thenReturn(Collections.emptyList());

    ResponseEntity<?> response = mockRouteController.searchItems(searchTerm);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("No items found", response.getBody());
  }

  @Test
  public void testDeleteClientNotFound() {
    int clientId = 1;
    Client client = new Client("test", "1,1");
    client.setId(clientId);

    when(mockClientRepository.existsById(clientId)).thenReturn(false);

    ResponseEntity<?> response = mockRouteController.deleteClient(clientId);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  public void testDeleteClientFound() {
    int clientId = 1;
    Client client = new Client("test", "1,1");
    client.setId(clientId);

    when(mockClientRepository.existsById(clientId)).thenReturn(true);

    ResponseEntity<?> response = mockRouteController.deleteClient(clientId);

    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  public void testDeleteItemNotFound() {
    int itemId = 1;

    when(mockItemRepository.existsById(itemId)).thenReturn(false);

    ResponseEntity<?> response = mockRouteController.deleteItem(itemId);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  public void testDeleteItemFound() {
    int itemId = 1;

    when(mockItemRepository.existsById(itemId)).thenReturn(true);

    ResponseEntity<?> response = mockRouteController.deleteItem(itemId);

    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  public void testDeleteOrganizationNotFound() {
    int orgId = 1;

    when(mockOrganizationRepository.existsById(orgId)).thenReturn(false);

    ResponseEntity<?> response = mockRouteController.deleteOrganization(orgId);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  public void testDeleteOrganizationFound() {
    int orgId = 1;

    when(mockOrganizationRepository.existsById(orgId)).thenReturn(true);

    ResponseEntity<?> response = mockRouteController.deleteOrganization(orgId);

    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  public void testGetOrganizationItems() {
    int orgId = 1;

    List<Item> mockItems = Arrays.asList(
            new Item(1, "name", "description", 1, orgId)
    );

    when(mockItemRepository.findByOrganizationId(orgId)).thenReturn(mockItems);

    ResponseEntity<?> response = mockRouteController.getOrganizationItems(orgId);

    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  public void testGetOrganizationItems_notFound() {
    int orgId = -1;

    List<Item> mockItems = Collections.emptyList();

    when(mockItemRepository.findByOrganizationId(orgId)).thenReturn(mockItems);

    ResponseEntity<?> response = mockRouteController.getOrganizationItems(orgId);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  public void testUpdateItemDoesntExist() {
    Integer itemId = 1;
    Item updatedItem = new Item(defaultItem.getId(), "Updated Client",
            "test", 10,
            defaultItem.getOrganizationId());

    when(mockItemRepository.existsById(itemId)).thenReturn(false);

    ResponseEntity<?> response = mockRouteController.updateItem(itemId, updatedItem);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

  }

  @Test
  public void testUpdateOrganizationExists() {
    Integer orgId = 1;
    Organization updatedOrganization = new Organization("Name", "1,0");

    when(mockOrganizationRepository.existsById(orgId)).thenReturn(true);

    ResponseEntity<?> response = mockRouteController.updateOrganization(orgId, updatedOrganization);

    assertEquals(HttpStatus.OK, response.getStatusCode());

  }

  @Test
  public void testUpdateOrganizationDoesntExists() {
    Integer orgId = 1;
    Organization updatedOrganization = new Organization("Name", "1,0");

    when(mockOrganizationRepository.existsById(orgId)).thenReturn(false);

    ResponseEntity<?> response = mockRouteController.updateOrganization(orgId, updatedOrganization);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

  }


  @Test
  public void testResolveDistance_InvalidOriginType() {
    ResponseEntity<?> response = mockRouteController.resolveDistance(1,
            "invalidType", 2, "client");

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("Invalid origin type", response.getBody());
  }

  @Test
  public void testResolveDistance_OriginNotFound() {
    when(mockClientRepository.findById(1)).thenReturn(Optional.empty());

    ResponseEntity<?> response = mockRouteController.resolveDistance(1,
            "client", 2, "client");

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("Origin client not found", response.getBody());
  }

  @Test
  public void testResolveDistance_DestinationNotFound() {
    when(mockClientRepository.findById(1)).thenReturn(Optional.of(defaultClient));
    when(mockOrganizationRepository.findById(2)).thenReturn(Optional.empty());

    ResponseEntity<?> response = mockRouteController.resolveDistance(1,
            "client", 2, "organization");

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("Destination organization not found", response.getBody());
  }

  @Test
  public void testResolveDistance_InvalidCoordinates() {
    ResponseEntity<?> response = mockRouteController.resolveDistance(1,
            "client", 2, "organization");

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("Origin client not found", response.getBody());
  }

  @Test
  public void testResolveDistance_OrganizationToOrganization_Success() {
    final Integer originId = 1;
    final Integer destId = 2;
    final String originType = "organization";
    final String destType = "organization";

    Organization originOrganization = new Organization();
    originOrganization.setLatitude(40.748817);  // Empire State Building latitude
    originOrganization.setLongitude(-73.985428); // Empire State Building longitude

    Organization destOrganization = new Organization();
    destOrganization.setLatitude(37.774929);  // San Francisco latitude
    destOrganization.setLongitude(-122.419418); // San Francisco longitude

    when(mockOrganizationRepository.findById(originId)).thenReturn(Optional.of(originOrganization));
    when(mockOrganizationRepository.findById(destId)).thenReturn(Optional.of(destOrganization));

    when(mockGooglePlacesService.getDistanceBetweenLocations(anyDouble(), anyDouble(),
            anyDouble(), anyDouble()))
            .thenReturn(500.0); // Example distance in meters

    ResponseEntity<?> response = mockRouteController.resolveDistance(originId, originType,
            destId, destType);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(500.0, response.getBody());
  }

  @Test
  public void testResolveDistance_InvalidOriginCoordinates() {
    final Integer originId = 1;
    final Integer destId = 2;
    final String originType = "client";
    final String destType = "organization";

    Client originClient = new Client();
    originClient.setLatitude(-91.0);  // Invalid latitude
    originClient.setLongitude(-181.0); // Invalid longitude

    Organization destOrganization = new Organization();
    destOrganization.setLatitude(37.774929);
    destOrganization.setLongitude(-122.419418);

    when(mockClientRepository.findById(originId)).thenReturn(Optional.of(originClient));
    when(mockOrganizationRepository.findById(destId)).thenReturn(Optional.of(destOrganization));

    ResponseEntity<?> response = mockRouteController.resolveDistance(originId, originType,
            destId, destType);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("Invalid location format", response.getBody());
  }

  @Test
  public void testResolveDistance_InvalidDestinationType() {
    final Integer originId = 1;
    final Integer destId = 2;
    final String originType = "client";
    final String destType = "FAKE"; //Invalid destType

    Client originClient = new Client();
    originClient.setLatitude(0.0);
    originClient.setLongitude(0.0);

    Organization destOrganization = new Organization();
    destOrganization.setLatitude(37.774929);
    destOrganization.setLongitude(-122.419418);

    when(mockClientRepository.findById(originId)).thenReturn(Optional.of(originClient));
    when(mockOrganizationRepository.findById(destId)).thenReturn(Optional.of(destOrganization));

    ResponseEntity<?> response = mockRouteController.resolveDistance(originId, originType,
            destId, destType);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("Invalid destination type", response.getBody());
  }

  @Test
  public void testResolveDistance_ClientToClient_Success() {
    final Integer originId = 1;
    final Integer destId = 2;
    final String originType = "client";
    final String destType = "client";

    Client originClient = new Client();
    originClient.setLatitude(40.748817);  // Empire State Building latitude
    originClient.setLongitude(-73.985428); // Empire State Building longitude

    Client destClient = new Client();
    destClient.setLatitude(37.774929);  // San Francisco latitude
    destClient.setLongitude(-122.419418); // San Francisco longitude

    when(mockClientRepository.findById(originId)).thenReturn(Optional.of(originClient));
    when(mockClientRepository.findById(destId)).thenReturn(Optional.of(destClient));
    when(mockGooglePlacesService.getDistanceBetweenLocations(anyDouble(), anyDouble(),
            anyDouble(), anyDouble()))
            .thenReturn(500.0); // Example distance in meters

    ResponseEntity<?> response = mockRouteController.resolveDistance(originId, originType,
            destId, destType);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(500.0, response.getBody());
  }

  @Test
  public void testRetrieveOrganization_Success() {
    Integer organizationId = defaultOrganization.getOrganizationId();

    when(mockOrganizationRepository.findById(organizationId)).thenReturn(Optional
            .of(defaultOrganization));

    ResponseEntity<?> response = mockRouteController.retrieveOrganization(organizationId);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    Organization retrievedOrganization = (Organization) response.getBody();

    // Print the organization details
    System.out.println("ID: " + retrievedOrganization.getOrganizationId());
    System.out.println("Name: " + retrievedOrganization.getName());
    System.out.println("Location: " + retrievedOrganization.getLocation());
    System.out.println("Latitude: " + retrievedOrganization.getLatitude());
    System.out.println("Longitude: " + retrievedOrganization.getLongitude());
  }

  @Test
  public void testRetrieveClient_Success() {
    Integer clientId = defaultClient.getId();

    when(mockClientRepository.findById(clientId)).thenReturn(Optional.of(defaultClient));

    ResponseEntity<?> response = mockRouteController.retrieveClient(clientId);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(defaultClient, response.getBody());
  }

  @Test
  public void testRetrieveClient_NotFound() {
    Integer clientId = 999; // Non-existing clientId

    when(mockClientRepository.findById(clientId)).thenReturn(Optional.empty());

    ResponseEntity<?> response = mockRouteController.retrieveClient(clientId);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("Client not found", response.getBody());
  }

  @Test
  public void testCreateClient_Success() {
    Client client = new Client();
    client.setName("New Client");

    when(mockClientRepository.save(any(Client.class))).thenReturn(client);

    ResponseEntity<?> response = mockRouteController.createClient(client);

    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertEquals(client, response.getBody());
  }

  @Test
  public void testRetrieveItem_Success() {
    Integer itemId = 1;
    Item item = new Item();
    item.setId(itemId);
    item.setName("Test Item");

    when(mockItemRepository.findById(itemId)).thenReturn(Optional.of(item));

    ResponseEntity<?> response = mockRouteController.retrieveItem(itemId);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(item, response.getBody());
  }

  @Test
  public void testRetrieveItem_NotFound() {
    Integer itemId = 1;

    when(mockItemRepository.findById(itemId)).thenReturn(Optional.empty());

    ResponseEntity<?> response = mockRouteController.retrieveItem(itemId);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("Item not found", response.getBody());
  }

  @Test
  public void testUpdateClient_NullClient() {
    Integer clientId = defaultClient.getId();
    ResponseEntity<?> response = mockRouteController.updateClient(clientId, null);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("Client not found", response.getBody());
  }

  @Test
  public void testUpdateItem_NullItem() {
    Integer itemId = defaultItem.getId();
    ResponseEntity<?> response = mockRouteController.updateItem(itemId, null);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("Item not found", response.getBody());
  }

  @Test
  public void testDeleteClient_NegativeId() {
    int clientId = -1;
    ResponseEntity<?> response = mockRouteController.deleteClient(clientId);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("Client not found", response.getBody());
  }

  @Test
  public void testDeleteItem_NegativeId() {
    int itemId = -1;
    ResponseEntity<?> response = mockRouteController.deleteItem(itemId);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("Item not found", response.getBody());
  }

  @Test
  public void testRetrieveOrganization_NegativeId() {
    Integer orgId = -1;
    ResponseEntity<?> response = mockRouteController.retrieveOrganization(orgId);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("Organization not found", response.getBody());
  }

}
