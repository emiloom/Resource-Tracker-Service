package com.restinginbed.teamproject;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.restinginbed.teamproject.model.Organization;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

/**
 * Unit Tests for Organization Class.
 */
@SpringBootTest
@ContextConfiguration
public class OrganizationUnitTests {
  
  private static Organization testOrganization;

  @BeforeAll
  public static void setUp() {
    testOrganization = new Organization("testOrganization", "Columbia University");
  }

  @Test
  public void setName_success() {
    testOrganization.setName("school");
    assertEquals("school", testOrganization.getName());
  }

  @Test
  public void setOrganizationId_success() {
    testOrganization.setOrganizationId(1);
    assertEquals(1, testOrganization.getOrganizationId());
  }
}