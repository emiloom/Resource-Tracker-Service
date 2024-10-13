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
public class ItemUnitTests {

  @Test
  public void ItemUnitTest() {
    int id = 1;
    String name = "Banana";
    String description = "Banana Description";
    int count = 1;
    String organization = "Banana Organization";


    Item itemTest = new Item(id, name, description, count, organization);

    assertEquals(itemTest.getId(), id);
    assertEquals(itemTest.getName(), name);
    assertEquals(itemTest.getDescription(), description);
    assertEquals(itemTest.getCount(), count);
    assertEquals(itemTest.getOrganization(), organization);

  }

  @Test
  public void addCountTest() {
    int id = 0;
    String name = "Banana";
    String organization = "Banana Organization";

    Item itemTest = new Item(id, name, organization);

    assertEquals(itemTest.getCount(), 0);

    itemTest.addCount(1);

    assertEquals(itemTest.getCount(), 1);

  }

  @Test
  public void removeCountTest() {
    int id = 0;
    String name = "Banana";
    String organization = "Banana Organization";

    Item itemTest = new Item(id, name, organization);

    assertEquals(itemTest.getCount(), 0);

    boolean result = itemTest.removeCount(1);

    assertEquals(itemTest.getCount(), 0);
    assertEquals(result, false);

  }



}