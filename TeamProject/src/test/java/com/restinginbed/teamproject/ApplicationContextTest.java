package com.restinginbed.teamproject;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

// import com.restinginbed.teamproject.config.OAuth2Config;

/**
 * Test class to verify that the Spring application context loads correctly.
 */
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
