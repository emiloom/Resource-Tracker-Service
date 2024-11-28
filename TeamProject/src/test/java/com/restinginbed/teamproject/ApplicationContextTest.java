package com.restinginbed.teamproject;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
// import com.restinginbed.teamproject.config.OAuth2Config;

@SpringBootTest
// @ImportAutoConfiguration(exclude = {OAuth2Config.class})
public class ApplicationContextTest {
    @Test
    void contextLoads() {
        // Ensure the context loads successfully
    // 

    // @TestConfiguration
    // static class TestConfig {
    //     @Bean
    //     public OAuth2AuthorizedClientService authorizedClientService() {
    //         return null; // Mock implementation for tests
    //     }
    }
}
