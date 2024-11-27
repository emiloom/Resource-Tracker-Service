package com.restinginbed.teamproject;

import com.restinginbed.teamproject.service.GooglePlacesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
public class GooglePlacesServiceTests {

    @Autowired
    private GooglePlacesService googlePlacesService;

    @Test
    public void testGetPlaceDetails() {
        String query = "Empire State Building";
        String response = googlePlacesService.getPlaceDetails(query);
        // System.out.println("Place Details: " + response);
    }

    @Test
    public void testGetDistanceBetweenLocations() {
        double originLat = 40.748817; // Empire State Building
        double originLng = -73.985428;
        double destLat = 40.689247; // Statue of Liberty
        double destLng = -74.044502;
        double response = googlePlacesService.getDistanceBetweenLocations(originLat, originLng, destLat, destLng);
        
        // Assert that the response is not null or empty
        assertNotNull(response);
        System.out.println("Distance: " + response);
    }

}
