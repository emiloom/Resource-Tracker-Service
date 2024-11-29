package com.restinginbed.teamproject.service;

import com.restinginbed.teamproject.model.Client;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Represents the Client Service.
 */
@Service
public class ClientService {
  @Autowired
  private GooglePlacesService googlePlacesService;

  /**
   * Updates the latitude and longitude of an organization based on its location.
   *
   * @param client        organization the organization entity to update
   */
  public void updateOrganizationCoordinates(Client client) {
    if (client.getLocation() != null && !client.getLocation().isEmpty()) {
      List<Double> coordinates = googlePlacesService.getPlaceCoordinates(client.getLocation());
      if (coordinates != null && coordinates.size() == 2) {
        client.setLatitude(coordinates.get(0));
        client.setLongitude(coordinates.get(1));
      }
    }
  }
}
