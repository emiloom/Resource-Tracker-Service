package com.restinginbed.teamproject.service;

import java.util.Arrays;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
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
  public double getDistanceBetweenLocations(
          double originLat, double originLng, double destLat, double destLng) {
    RestTemplate restTemplate = new RestTemplate();
    String url = DISTANCE_MATRIX_API_URL + "?origins=" + originLat + ","
            + originLng + "&destinations=" + destLat + "," + destLng + "&key=" + apiKey;
    String response = restTemplate.getForObject(url, String.class);
    
    JSONObject jsonResponse = new JSONObject(response);
    JSONArray rows = jsonResponse.getJSONArray("rows");
    if (rows.length() > 0) {
      JSONArray elements = rows.getJSONObject(0).getJSONArray("elements");
      if (elements.length() > 0) {
        JSONObject distance = elements.getJSONObject(0).getJSONObject("distance");
        // Return the distance in kilometers as a double
        return distance.getDouble("value") / 1000.0;
      }
    }
    return 0.0; // Return 0.0 if no distance found
  }

  /**
   * Gets coordinates based off of query.
   *
   * @param query     user inputted query
   * @return          Returns latitude and longitude as list of doubles
   */
  public List<Double> getPlaceCoordinates(String query) {
    RestTemplate restTemplate = new RestTemplate();
    
    // get placeid
    String autocompleteUrl = PLACES_API_URL + "?input=" + query + "&key=" + apiKey;
    String autocompleteResponse = restTemplate.getForObject(autocompleteUrl, String.class);
    
    JSONObject autocompleteJson = new JSONObject(autocompleteResponse);
    JSONArray predictions = autocompleteJson.getJSONArray("predictions");
    if (predictions.length() == 0) {
      return Arrays.asList(0.0, 0.0);
    }
    String placeId = predictions.getJSONObject(0).getString("place_id");
    
    // get lat and long
    String detailsUrl = "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + placeId + "&key=" + apiKey;
    String detailsResponse = restTemplate.getForObject(detailsUrl, String.class);
    
    JSONObject detailsJson = new JSONObject(detailsResponse);
    JSONObject location = detailsJson.getJSONObject("result")
            .getJSONObject("geometry")
            .getJSONObject("location");
    double latitude = location.getDouble("lat");
    double longitude = location.getDouble("lng");
    
    return Arrays.asList(latitude, longitude);
  }
}
