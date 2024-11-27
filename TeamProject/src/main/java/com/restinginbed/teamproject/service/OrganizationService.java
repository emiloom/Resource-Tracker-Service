package com.restinginbed.teamproject.service;

import com.restinginbed.teamproject.model.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing Organization entities.
 */
@Service
public class OrganizationService {

    @Autowired
    private GooglePlacesService googlePlacesService;

    /**
     * Updates the latitude and longitude of an organization based on its location.
     *
     * @param organization the organization entity to update
     */
    public void updateOrganizationCoordinates(Organization organization) {
        if (organization.getLocation() != null && !organization.getLocation().isEmpty()) {
            List<Double> coordinates = googlePlacesService.getPlaceCoordinates(organization.getLocation());
            if (coordinates != null && coordinates.size() == 2) {
                organization.setLatitude(coordinates.get(0));
                organization.setLongitude(coordinates.get(1));
            }
        }
    }
}
