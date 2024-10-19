package com.restinginbed.teamproject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;



/**
 * Unit test cases for Client class.
 */
@SpringBootTest
@ContextConfiguration
public class ClientUnitTests {
  /** The test Client instance used for testing. */
  public static Client testClient;

  @BeforeAll
  public static void setupCourseForTesting() {
    testClient = new Client();
  }

  @Test
  public void ClientClassTest() {
    int id = 1;
    String name = "Test Client";
    String location = "1.1, 1.2";

    Client testClient = new Client (name, location);

    assertEquals(testClient.getLocation(), location);
    assertEquals(testClient.getLongitude(), 1.2);
    assertEquals(testClient.getLatitude(), 1.1);
    assertEquals(testClient.getName(), name);


  }

  @Test
  public void setLongitudeFirstTest() {
    Client testClient = new Client("test");

    String location = "1.1, 1.2";
    double longitude = 1.2;
    double latitude = 1.1;

    testClient.setLongitude(longitude);
    testClient.setLatitude(latitude);

    assertEquals(testClient.getLocation(), location);
    assertEquals(testClient.getLongitude(), longitude);
    assertEquals(testClient.getLatitude(), latitude);

  }

  @Test
  public void setLatitudeFirstTest() {
    Client testClient = new Client("test");

    String location = "1.1, 1.2";
    double longitude = 1.2;
    double latitude = 1.1;
    String newName = "test2";

    testClient.setName(newName);
    testClient.setLatitude(latitude);
    testClient.setLongitude(longitude);

    assertEquals(testClient.getName(), newName);
    assertEquals(testClient.getLocation(), location);
    assertEquals(testClient.getLongitude(), longitude);
    assertEquals(testClient.getLatitude(), latitude);

  }

  @Test
  public void setInvalidLocation() {
    Client testClient = new Client("test");

    String location = "1.1, 1.2, 1.3";

    testClient.setLocation(location);

    assertThrows(IllegalArgumentException.class, () -> {
      testClient.getLongitude();
    });

    assertThrows(IllegalArgumentException.class, () -> {
      testClient.getLatitude();
    });

  }

  @Test
  public void setInvalidLocationDataType() {
    Client testClient = new Client("test");

    String location = "hi, hello";

    testClient.setLocation(location);

    assertThrows(IllegalArgumentException.class, () -> {
      testClient.getLongitude();
    });

    assertThrows(IllegalArgumentException.class, () -> {
      testClient.getLatitude();
    });

  }

  @Test
  public void setNullLocation() {
    Client testClient = new Client("test");

    String location = null;

    testClient.setLocation(location);

    assertThrows(IllegalArgumentException.class, () -> {
      testClient.getLongitude();
    });

    assertThrows(IllegalArgumentException.class, () -> {
      testClient.getLatitude();
    });

  }

  @Test
  public void setLongitudeWhenLocationNull() {
    Client testClient = new Client("test");

    String location = null;
    double longitude = 1.2;
    double latitude = 1.1;

    testClient.setLocation(location);
    testClient.setLongitude(longitude);

    assertEquals(testClient.getLongitude(), longitude);

  }

}