package com.restinginbed.teamproject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;
import org.springframework.test.util.ReflectionTestUtils;

import com.restinginbed.teamproject.model.Client;
import com.restinginbed.teamproject.model.Item;
import com.restinginbed.teamproject.repository.ClientRepository;
import com.restinginbed.teamproject.repository.ItemRepository;
import com.restinginbed.teamproject.repository.OrganizationRepository;
import com.restinginbed.teamproject.service.GooglePlacesService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


/**
 * Unit Tests for RouteController Class.
 */
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

  /**
   * Initializes the necessary test objects and mocks before each test case.
   */
  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    defaultClient = new Client("test client", "1,1");
    defaultItem = new Item(0, "name", "test", 10, 0);;
  }

  @Test
  public void testUpdateClient_Success() {
    Integer clientId = defaultClient.getId();
    Client updatedClient = new Client("Updated Client", "1,1");
    updatedClient.setLocation("New Location");

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
    Client updatedClient = new Client("Updated Client", "1,1");

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
  //         assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  //         assertEquals("No items found", response.getBody());
  //     }

  // @Test
  // public void testRankNearestOrganizations_Success() {
  //   // Set up the client
  //   Client testClient = new Client("Test Client");
  //   testClient.setLocation("40.807536, -73.962573");  // Setting location as a string

  //   // Mock the GooglePlacesService behavior to return a specific location
  //   when(mockGooglePlacesService.getPlaceCoordinates(anyString()))
  //       .thenReturn("40.807000, -73.964000");  // Mock response for location query

  //   // Create Organizations and inject the mocked GooglePlacesService
  //   Organization org1 = new Organization("Org 1", "3009 Barnard College New York");
  //   Organization org2 = new Organization("Org 2", "Stuyvesant High School New York");
  //   Organization org3 = new Organization("Org 3", "Cornell University Ithaca, New York");

  //   // Use ReflectionTestUtils to inject mock GooglePlacesService into each organization
  //   ReflectionTestUtils.setField(org1, "googlePlacesService", mockGooglePlacesService);
  //   ReflectionTestUtils.setField(org2, "googlePlacesService", mockGooglePlacesService);
  //   ReflectionTestUtils.setField(org3, "googlePlacesService", mockGooglePlacesService);

  //   // Mock repository behavior to return the test client and organizations
  //   when(mockClientRepository.findById(1)).thenReturn(Optional.of(testClient));
  //   when(mockOrganizationRepository.findAll()).thenReturn(List.of(org1, org2, org3));

  //   // Mock the distance calculation for each pair of locations
  //   String distanceResponse = "{\"rows\":[{\"elements\":[{\"distance\":{\"value\":100}}]}]}";
  //   when(mockGooglePlacesService.getDistanceBetweenLocations(anyDouble(), anyDouble(), anyDouble(), anyDouble()))
  //       .thenReturn(distanceResponse);

  //   // Call the method under test
  //   ResponseEntity<?> response = mockRouteController.rankNearestOrganizations(1, "client");

  //   // Assert the response is not null and has an OK status
  //   assertNotNull(response, "Response should not be null");
  //   assertEquals(HttpStatus.OK, response.getStatusCode());

  //   // Check the response body to ensure it contains a list of organizations
  //   Object responseBody = response.getBody();
  //   if (responseBody instanceof List<?>) {
  //       List<?> list = (List<?>) responseBody;
  //       if (!list.isEmpty() && list.get(0) instanceof OrganizationDistanceDataTransferObject) {
  //           List<OrganizationDistanceDataTransferObject> rankedList = list.stream()
  //               .filter(OrganizationDistanceDataTransferObject.class::isInstance)
  //               .map(OrganizationDistanceDataTransferObject.class::cast)
  //               .collect(Collectors.toList());
  //           assertNotNull(rankedList);
  //           assertEquals(3, rankedList.size());  // Ensure 3 organizations are returned
  //       }
  //   }
  // }
}
