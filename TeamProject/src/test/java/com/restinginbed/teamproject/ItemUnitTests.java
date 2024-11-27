package com.restinginbed.teamproject;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import com.restinginbed.teamproject.model.Item;

/**
 * Unit test cases for the User class.
 */
@SpringBootTest
@ContextConfiguration
public class ItemUnitTests {

  @Test
  public void itemUnitTest() {
    int id = 1;
    String name = "Banana";
    String description = "Banana Description";
    int count = 1;
    int organizationId = 1;

    Item itemTest = new Item(id, name, description, count, organizationId);

    assertEquals(itemTest.getId(), id);
    assertEquals(itemTest.getName(), name);
    assertEquals(itemTest.getDescription(), description);
    assertEquals(itemTest.getCount(), count);
    assertEquals(itemTest.getOrganizationId(), organizationId);

  }

  @Test
  public void addCountTest() {
    int id = 0;
    String name = "Banana";
    int organizationId = 1;

    Item itemTest = new Item(id, name, "test", 0, organizationId);

    assertEquals(itemTest.getCount(), 0);

    itemTest.addCount(1);

    assertEquals(itemTest.getCount(), 1);

  }

  @Test
  public void removeCount_Fail() {
    int id = 0;
    String name = "Banana";
    int organizationId = 1;

    Item itemTest = new Item(id, name, "test", 0, organizationId);

    assertEquals(itemTest.getCount(), 0);

    boolean result = itemTest.removeCount(1);

    assertEquals(itemTest.getCount(), 0);
    assertEquals(result, false);
  }

  @Test
  public void removeCount_success() {
    int id = 0;
    String name = "Banana";
    int organizationId = 1;

    Item itemTest = new Item(id, name,"test", 0, organizationId);

    itemTest.addCount(5);
    assertEquals(itemTest.getCount(), 5);

    boolean result = itemTest.removeCount(1);

    assertEquals(itemTest.getCount(), 4);
    assertEquals(result, true);
  }

}