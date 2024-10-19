package com.restinginbed.teamproject;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

/**
 * Unit Tests for Organization Class.
 */
@SpringBootTest
@ContextConfiguration
public class OrganizationUnitTests {
  
  private static final int ORGANIZATION_ID = 1;
  private static final Item MOCK_ITEM = new Item(0, "mockItem", ORGANIZATION_ID);

  private static Organization testOrganization;
  
  @BeforeAll
  public static void setUp() {
    testOrganization = new Organization("testOrganization");
  }

  //  @Test
  //  public void addItem_success() {
  //    assertEquals(testOrganization.addItem(MOCK_ITEM), true);
  //  }

  //  @Test
  //  public void getItem_success() {
  //    testOrganization.addItem(MOCK_ITEM);
  //    assertEquals(testOrganization.getItem(0), MOCK_ITEM);
  //  }

  //  @Test
  //  public void deleteItem_success() {
  //    testOrganization.addItem(MOCK_ITEM);
  //    assertEquals(testOrganization.deleteItem(MOCK_ITEM), true);
  //  }
}