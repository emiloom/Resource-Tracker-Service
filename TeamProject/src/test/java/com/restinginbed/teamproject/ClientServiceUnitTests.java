package com.restinginbed.teamproject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.restinginbed.teamproject.model.Client;
import com.restinginbed.teamproject.service.ClientService;
import com.restinginbed.teamproject.service.GooglePlacesService;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Unit tests for ClientService class.
 */
public class ClientServiceUnitTests {

  @Mock
  private GooglePlacesService googlePlacesService;

  @InjectMocks
  private ClientService clientService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testUpdateOrganizationCoordinates_ValidCoordinates() {
    // Arrange
    Client client = new Client();
    client.setLocation("Valid Location");
    List<Double> mockCoordinates = Arrays.asList(40.7128, -74.0060);
    when(googlePlacesService.getPlaceCoordinates(
        "Valid Location")).thenReturn(mockCoordinates);

    // Act
    clientService.updateOrganizationCoordinates(client);

    // Assert
    assertEquals(40.7128, client.getLatitude());
    assertEquals(-74.0060, client.getLongitude());
    verify(googlePlacesService, times(1))
        .getPlaceCoordinates("Valid Location");
  }

  @Test
  void testUpdateOrganizationCoordinates_NullLocation() {
    // Arrange
    Client client = new Client();
    client.setLocation(null);

    // Act
    clientService.updateOrganizationCoordinates(client);

    assertEquals(0.0, client.getLatitude());
    assertEquals(0.0, client.getLongitude());
    verifyNoInteractions(googlePlacesService);
  }

  @Test
  void testUpdateOrganizationCoordinates_EmptyLocation() {
    // Arrange
    Client client = new Client();
    client.setLocation("");

    // Act
    clientService.updateOrganizationCoordinates(client);

    assertEquals(0.0, client.getLatitude());
    assertEquals(0.0, client.getLongitude());
    verifyNoInteractions(googlePlacesService);
  }

  @Test
  void testUpdateOrganizationCoordinates_NullCoordinatesFromService() {
    // Arrange
    Client client = new Client();
    client.setLocation("Valid Location");
    when(googlePlacesService.getPlaceCoordinates("Valid Location")).thenReturn(null);

    // Act
    clientService.updateOrganizationCoordinates(client);

    // Assert
    assertEquals(0.0, client.getLatitude());
    assertEquals(0.0, client.getLongitude());
    verify(googlePlacesService, times(1)).getPlaceCoordinates("Valid Location");
  }

  @Test
  void testUpdateOrganizationCoordinates_IncompleteCoordinates() {
    // Arrange
    Client client = new Client();
    client.setLocation("Valid Location");
    when(googlePlacesService.getPlaceCoordinates("Valid Location"))
        .thenReturn(Collections.singletonList(40.7128));

    // Act
    clientService.updateOrganizationCoordinates(client);

    assertEquals(0.0, client.getLatitude());
    assertEquals(0.0, client.getLongitude());
    verify(googlePlacesService, times(1))
        .getPlaceCoordinates("Valid Location");
  }

  @Test
  void testUpdateOrganizationCoordinates_EmptyCoordinatesFromService() {
    // Arrange
    Client client = new Client();
    client.setLocation("Valid Location");
    when(googlePlacesService.getPlaceCoordinates("Valid Location"))
        .thenReturn(Collections.emptyList());

    // Act
    clientService.updateOrganizationCoordinates(client);

    // Assert
    assertEquals(0.0, client.getLatitude());
    assertEquals(0.0, client.getLongitude());
    verify(googlePlacesService, times(1)).getPlaceCoordinates("Valid Location");
  }
}
