package com.restinginbed.teamproject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.util.ReflectionTestUtils;

import com.restinginbed.teamproject.model.Client;
import com.restinginbed.teamproject.model.Item;
import com.restinginbed.teamproject.repository.ClientRepository;
import com.restinginbed.teamproject.repository.ItemRepository;
import com.restinginbed.teamproject.repository.OrganizationRepository;
import com.restinginbed.teamproject.service.GooglePlacesService;
import com.restinginbed.teamproject.controller.RouteController;
import com.restinginbed.teamproject.dto.OrganizationDistanceDataTransferObject;
import com.restinginbed.teamproject.model.Organization;

import java.util.Collections;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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
    Item updatedItem = new Item(defaultItem.getId(), "Updated Client", "test", 10,
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
    List<Item> itemList = List.of(new Item(0, "Test Item", "test", 10, 0));

    when(mockItemRepository.findByNameContaining(searchTerm)).thenReturn(itemList);

    ResponseEntity<?> response = mockRouteController.searchItems(searchTerm);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(itemList, response.getBody());
  }

  @Test
  public void testSearchItems_ItemNotFound() {
    String searchTerm = "Test";

    List<Item> itemList = List.of(new Item(0, "name", "test", 10, 0));

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
  public void testUpdateItemDoesntExist() {
    Integer itemId = 1;
    Item updatedItem = new Item(defaultItem.getId(), "Updated Client", "test", 10,
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
    ResponseEntity<?> response = mockRouteController.resolveDistance(1, "invalidType", 2, "client");

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("Invalid origin type", response.getBody());
  }

  @Test
  public void testResolveDistance_OriginNotFound() {
    when(mockClientRepository.findById(1)).thenReturn(Optional.empty());

    ResponseEntity<?> response = mockRouteController.resolveDistance(1, "client", 2, "client");

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("Origin client not found", response.getBody());
  }

  @Test
  public void testResolveDistance_DestinationNotFound() {
    when(mockClientRepository.findById(1)).thenReturn(Optional.of(defaultClient));
    when(mockOrganizationRepository.findById(2)).thenReturn(Optional.empty());

    ResponseEntity<?> response = mockRouteController.resolveDistance(1, "client", 2, "organization");

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("Destination organization not found", response.getBody());
  }

  @Test
  public void testRetrieveOrganization_Success() {
    Integer organizationId = defaultOrganization.getOrganizationId();

    when(mockOrganizationRepository.findById(organizationId)).thenReturn(Optional.of(defaultOrganization));

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

  // @Test
  // public void testRankNearestOrganizations() {
  //   Integer originId = defaultOrganization.getOrganizationId();
  //   String originType = "organization";

  //   Organization org1 = new Organization();
  //   org1.setOrganizationId(1);
  //   org1.setLatitude(34.0522);
  //   org1.setLongitude(-118.2437);

  //   Organization org2 = new Organization();
  //   org2.setOrganizationId(3);
  //   org2.setLatitude(2.0);
  //   org2.setLongitude(-2.0);

  //   List<Organization> organizations = List.of(org1, org2);
  //   when(mockOrganizationRepository.findAll()).thenReturn(organizations);
  //   when(mockOrganizationRepository.findById(originId)).thenReturn(Optional.of(defaultOrganization));

  //   // Debugging: Log the origin organization details
  //   System.out.println("Origin ID: " + originId);
  //   System.out.println("Origin Type: " + originType);
  //   System.out.println("Default Organization Latitude: " + defaultOrganization.getLatitude());
  //   System.out.println("Default Organization Longitude: " + defaultOrganization.getLongitude());

  //   ResponseEntity<?> response = mockRouteController.rankNearestOrganizations(originId, originType);

  //   // Debugging: Check the organizations being processed
  //   for (Organization organization : organizations) {
  //       System.out.println("Processing Organization ID: " + organization.getOrganizationId());
  //       System.out.println("Latitude: " + organization.getLatitude());
  //       System.out.println("Longitude: " + organization.getLongitude());
  //   }

  //   // Print the response body
  //   System.out.println("Response Body: " + response.getBody());

  //   assertNotNull(response.getBody());
  //   // assertEquals(HttpStatus.OK, response.getStatusCode());
  //   // assertTrue(response.getBody() instanceof List);
  // }
}
