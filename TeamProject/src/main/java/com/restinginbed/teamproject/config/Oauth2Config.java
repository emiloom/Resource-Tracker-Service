package com.restinginbed.teamproject.config;

import java.util.Arrays;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuration class for setting up OAuth2 authentication in the application.
 *
 * <p>This class defines the necessary beans and methods to configure OAuth2
 * authentication with Google as the identity provider. It includes the
 * client registration details and the security filter chain for handling
 * OAuth2 login.
 */
@Configuration
public class Oauth2Config {
  @Value("${spring.security.oauth2.client.registration.google.client-id}")
  private String googleClientId;

  @Value("${spring.security.oauth2.client.registration.google.client-secret}")
  private String googleClientSecret;

  /**
   * Configures a repository for managing OAuth2 client registrations.
   *
   * @return a repository containing the Google client registration.
   */
  @Bean
  public ClientRegistrationRepository clientRegistrationRepository() {
    return new InMemoryClientRegistrationRepository(Arrays.asList(
            getGoogleClientRegistration()
    ));
  }

  private ClientRegistration getGoogleClientRegistration() {
    return ClientRegistration.withRegistrationId("google")
            .clientId(googleClientId)
            .clientSecret(googleClientSecret)
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
            .scope("openid", "profile", "email")
            .authorizationUri("https://accounts.google.com/o/oauth2/auth")
            .tokenUri("https://www.googleapis.com/oauth2/v3/token")
            .userInfoUri("https://www.googleapis.com/oauth2/v3/userinfo")
            .userNameAttributeName(IdTokenClaimNames.SUB)
            .clientName("Google")
            .build();
  }

  /**
   * Configures the security filter chain for the application.
   *
   * <p>Disables CSRF protection and allows all requests. Configures OAuth2
   * login with a custom login page for Google authentication.
   *
   * @param http the HttpSecurity object to configure security settings.
   * @return the configured SecurityFilterChain.
   */
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authorize -> authorize
                    .anyRequest().permitAll()
            )
            .oauth2Login(oauth2 -> oauth2
                    .loginPage("/oauth2/authorization/google")
        );
    return http.build();
  }

}