package com.restinginbed.teamproject.service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class GoogleAuthService {

  private static final String TOKEN_INFO_URL = "https://www.googleapis.com/oauth2/v3/tokeninfo";

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