package com.restinginbed.TeamProject;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

/**
 * Unit test cases for User class.
 */
@SpringBootTest
@ContextConfiguration
public class UserUnitTests {
//  /** The test User instance used for testing. */
//  public static User testUser;
//
//  @BeforeAll
//  public static void setupCourseForTesting() {
//    testUser = new User(123, 1.1, 2.2);
//  }

  @Test
  public void userClassTest() {
    int id = 1;
    double longitude = 1.1;
    double latitude = 1.2;

    User testUser = new User (id, longitude, latitude);

    assertEquals(testUser.getId(), id);
    assertEquals(testUser.getLongitude(), longitude);
    assertEquals(testUser.getLatitude(), latitude);
  }



}