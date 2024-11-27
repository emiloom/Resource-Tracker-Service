package com.restinginbed.teamproject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.restinginbed.teamproject.jparepositories.ClientRepository;
import com.restinginbed.teamproject.jparepositories.ItemRepository;
import com.restinginbed.teamproject.jparepositories.OrganizationRepository;
import java.util.Collections;
import java.util.Arrays;
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

}
