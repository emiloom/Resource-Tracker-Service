package com.restinginbed.teamproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

/**
 * Controller that handles OAuth2 login success and processes the OAuth2 token
 * for a specified provider.
 * 
 * <p>This controller handles the callback from an OAuth2
 * provider after authentication. Retrieves the authorized client
 * information, processes the authentication token, and performs actions such as
 * storing user details or generating a JWT token for the user.</p>
 */
@RestController
public class OAuth2Controller {
  private final OAuth2AuthorizedClientService clientService;

  @Autowired
  public OAuth2Controller(OAuth2AuthorizedClientService clientService) {
    this.clientService = clientService;
  }
 
  /**
   * Handles the successful OAuth2 login and performs necessary actions such as
   * storing user information or generating a JWT token, then redirects the user
   * to a home page or any other post-login page.
   *
   * @param provider the OAuth2 provider's name from the URL path
   * @param authenticationToken the OAuth2AuthenticationToken that contains the
   *                            authentication details of the user
   * @return redirects the user to the home page after successful login
   */
  @GetMapping("/login/oauth2/code/{provider}")
  public RedirectView loginSuccess(@PathVariable String provider, 
          OAuth2AuthenticationToken authenticationToken) {
    OAuth2AuthorizedClient client = clientService.loadAuthorizedClient(
            authenticationToken.getAuthorizedClientRegistrationId(),
            authenticationToken.getName()
    );

    // Handle storing the user information and token, then redirect to a successful 
    //login page For example, store user details in your database and generate a
    //JWT token for further authentication.

    return new RedirectView("/home");
  }
}