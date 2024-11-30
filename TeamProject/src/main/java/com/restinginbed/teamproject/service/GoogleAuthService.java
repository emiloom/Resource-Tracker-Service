package com.restinginbed.teamproject.service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * Service class for handling Google OAuth2 authentication.
 *
 * <p>Provides methods to validate access tokens and retrieve user information.
 */
public class GoogleAuthService {

  private static final String TOKEN_INFO_URL = "https://www.googleapis.com/oauth2/v3/tokeninfo";

  /**
   * Retrieves the user ID from the provided Google OAuth2 access token.
   *
   * @param token the access token to validate and extract user information.
   * @return the user ID as a string if the token is valid.
   * @throws Exception if there is an error connecting to the token info
   *                   endpoint or processing the response.
   */
  public String getUserIdFromToken(String token) throws Exception {
    URL url = new URL(TOKEN_INFO_URL + "?access_token=" + token);
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setRequestMethod("GET");

    try (Scanner scanner = new Scanner(connection.getInputStream())) {
      String response = scanner.useDelimiter("\\A").next();
      System.out.println("Token info response: " + response);

      return response;
    } catch (IOException e) {
      throw new IllegalArgumentException("Invalid access token.");
    }
  }
}