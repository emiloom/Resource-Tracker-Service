package com.restinginbed.teamproject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.restinginbed.teamproject.model.Organization;
import com.restinginbed.teamproject.service.GooglePlacesService;
import com.restinginbed.teamproject.service.OrganizationService;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Unit tests for OrganizationService class.
 */
public class OrganizationServiceUnitTests {

  @Mock
  private GooglePlacesService googlePlacesService;

  @InjectMocks
  private OrganizationService organizationService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testUpdateOrganizationCoordinates_ValidCoordinates() {
    // Arrange
    Organization organization = new Organization();
    organization.setLocation("Valid Location");
    List<Double> mockCoordinates = Arrays.asList(40.7128, -74.0060);
    when(googlePlacesService.getPlaceCoordinates("Valid Location"))
        .thenReturn(mockCoordinates);

    // Act
    organizationService.updateOrganizationCoordinates(organization);

    // Assert
    assertEquals(40.7128, organization.getLatitude());
    assertEquals(-74.0060, organization.getLongitude());
    verify(googlePlacesService, times(1))
        .getPlaceCoordinates("Valid Location");
  }

  @Test
  void testUpdateOrganizationCoordinates_NullLocation() {
    // Arrange
    Organization organization = new Organization();
    organization.setLocation(null);

    // Act
    organizationService.updateOrganizationCoordinates(organization);

    // Assert
    assertEquals(0.0, organization.getLatitude());
    assertEquals(0.0, organization.getLongitude());
    verifyNoInteractions(googlePlacesService);
  }

  @Test
  void testUpdateOrganizationCoordinates_EmptyLocation() {
    // Arrange
    Organization organization = new Organization();
    organization.setLocation("");

    // Act
    organizationService.updateOrganizationCoordinates(organization);

    // Assert
    assertEquals(0.0, organization.getLatitude());
    assertEquals(0.0, organization.getLongitude());
    verifyNoInteractions(googlePlacesService);
  }

  @Test
  void testUpdateOrganizationCoordinates_NullCoordinatesFromService() {
    // Arrange
    Organization organization = new Organization();
    organization.setLocation("Valid Location");
    when(googlePlacesService.getPlaceCoordinates("Valid Location")).thenReturn(null);

    // Act
    organizationService.updateOrganizationCoordinates(organization);

    // Assert
    assertEquals(0.0, organization.getLatitude());
    assertEquals(0.0, organization.getLongitude());
    verify(googlePlacesService, times(1))
        .getPlaceCoordinates("Valid Location");
  }

  @Test
  void testUpdateOrganizationCoordinates_IncompleteCoordinates() {
    // Arrange
    Organization organization = new Organization();
    organization.setLocation("Valid Location");
    when(googlePlacesService.getPlaceCoordinates("Valid Location"))
        .thenReturn(Collections.singletonList(40.7128));

    // Act
    organizationService.updateOrganizationCoordinates(organization);

    // Assert
    assertEquals(0.0, organization.getLatitude());
    assertEquals(0.0, organization.getLongitude());
    verify(googlePlacesService, times(1))
        .getPlaceCoordinates("Valid Location");
  }

  @Test
  void testUpdateOrganizationCoordinates_EmptyCoordinatesFromService() {
    // Arrange
    Organization organization = new Organization();
    organization.setLocation("Valid Location");
    when(googlePlacesService.getPlaceCoordinates("Valid Location"))
        .thenReturn(Collections.emptyList());

    // Act
    organizationService.updateOrganizationCoordinates(organization);

    // Assert
    assertEquals(0.0, organization.getLatitude());
    assertEquals(0.0, organization.getLongitude());
    verify(googlePlacesService, times(1))
        .getPlaceCoordinates("Valid Location");
  }
}
