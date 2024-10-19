package com.restinginbed.teamproject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * A service class that interacts with the Google Places API and Google Distance Matrix API.
 */
@Service
public class GooglePlacesService {
  @Value("${google.api.key}")
  private String apiKey;

  private static final String PLACES_API_URL = "https://maps.googleapis.com/maps/api/place/autocomplete/json";
  private static final String DISTANCE_MATRIX_API_URL = "https://maps.googleapis.com/maps/api/distancematrix/json";

  /**
   * Retrieves place details from the Google Places API using a query string.
   *
   * @param query         the search query to find place details
   * @return              the place details as a JSON string
   */
  public String getPlaceDetails(String query) {
    RestTemplate restTemplate = new RestTemplate();
    String url = PLACES_API_URL + "?input=" + query + "&key=" + apiKey;
    return restTemplate.getForObject(url, String.class);
  }

  /**
   * Retrieves the distance between two geographic locations using the Google Distance Matrix API.
   *
   * @param originLat         the latitude of the origin location
   * @param originLng         the longitude of the origin location
   * @param destLat           the latitude of the destination location
   * @param destLng           the longitude of the destination location
   * @return                  the distance between the two locations as a JSON string
   */
  public String getDistanceBetweenLocations(
          double originLat, double originLng, double destLat, double destLng) {
    RestTemplate restTemplate = new RestTemplate();
    String url = DISTANCE_MATRIX_API_URL + "?origins=" + originLat + ","
            + originLng + "&destinations=" + destLat + "," + destLng + "&key=" + apiKey;
    return restTemplate.getForObject(url, String.class);
  }
}
