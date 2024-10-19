package com.restinginbed.teamproject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.restinginbed.teamproject.jparepositories.ClientRepository;
import com.restinginbed.teamproject.jparepositories.ItemRepository;
import com.restinginbed.teamproject.jparepositories.OrganizationRepository;
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
    defaultClient = new Client("test client");
    defaultItem = new Item(0, "Test Item", 0);
  }

  @Test
  public void testUpdateClient_Success() {
    Integer clientId = defaultClient.getId();
    Client updatedClient = new Client("Updated Client");
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
    Client updatedClient = new Client("Updated Client");

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
    List<Item> itemList = List.of(new Item(0, "Test Item", 0));

    when(mockItemRepository.findByNameContaining(searchTerm)).thenReturn(itemList);

    ResponseEntity<?> response = mockRouteController.searchItems(searchTerm);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(itemList, response.getBody());
  }


  //     @Test
  //     public void testSearchItems_ItemNotFound() {
  //         String searchTerm = "Test";

  //         List<Item> itemList = List.of(new Item(0, "Test Item", 0));

  //         when(mockItemRepository.findByNameContaining(searchTerm)).thenReturn(itemList);

  //         ResponseEntity<?> response = mockRouteController.searchItems(searchTerm);

  //         assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  //         assertEquals("No items found", response.getBody());
  //     }
}
