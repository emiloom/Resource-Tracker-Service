package com.restinginbed.teamproject;

import com.restinginbed.teamproject.controller.OAuth2Controller;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.servlet.view.RedirectView;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class OAuth2ControllerUnitTests {

    @InjectMocks
    private OAuth2Controller oAuth2Controller;

    @Mock
    private OAuth2AuthorizedClientService clientService;

    @Mock
    private OAuth2AuthenticationToken authenticationToken;

    @Mock
    private OAuth2AuthorizedClient authorizedClient;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLoginSuccess() {
        String provider = "testProvider";
        String userName = "testUser";

        when(authenticationToken.getAuthorizedClientRegistrationId()).thenReturn(provider);
        when(authenticationToken.getName()).thenReturn(userName);
        when(clientService.loadAuthorizedClient(provider, userName)).thenReturn(authorizedClient);

        RedirectView result = oAuth2Controller.loginSuccess(provider, authenticationToken);

        assertEquals("/home", result.getUrl());
        verify(clientService).loadAuthorizedClient(provider, userName);
    }

}
