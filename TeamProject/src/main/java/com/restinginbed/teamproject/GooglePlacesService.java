package com.restinginbed.teamproject;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

@Service
public class GooglePlacesService {
  @Value("${google.api.key}")
  private String apiKey;

  private static final String PLACES_API_URL = "https://maps.googleapis.com/maps/api/place/autocomplete/json";
  private static final String DISTANCE_MATRIX_API_URL = "https://maps.googleapis.com/maps/api/distancematrix/json";

  public String getPlaceDetails(String query) {
    RestTemplate restTemplate = new RestTemplate();
    String url = PLACES_API_URL + "?input=" + query + "&key=" + apiKey;
    return restTemplate.getForObject(url, String.class);
  }

  public String getDistanceBetweenLocations(double originLat, double originLng, double destLat, double destLng) {
    RestTemplate restTemplate = new RestTemplate();
    String url = DISTANCE_MATRIX_API_URL + "?origins=" + originLat + "," + originLng + "&destinations="
      + destLat + "," + destLng + "&key=" + apiKey;
    return restTemplate.getForObject(url, String.class);
  }
}
