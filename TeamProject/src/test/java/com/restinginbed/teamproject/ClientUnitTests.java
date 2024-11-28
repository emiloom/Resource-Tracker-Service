package com.restinginbed.teamproject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.restinginbed.teamproject.model.Client;

class ClientTest {

    private Client client;

    @BeforeEach
    void setUp() {
        client = new Client("John Doe", "New York");
    }

    @Test
    void testClientConstructor() {
        assertEquals("John Doe", client.getName());
        assertEquals("New York", client.getLocation());
    }

    @Test
    void testSetName() {
        client.setName("Jane Doe");
        assertEquals("Jane Doe", client.getName());
    }

    @Test
    void testSetLocation() {
        client.setLocation("Los Angeles");
        assertEquals("Los Angeles", client.getLocation());
    }

    @Test
    void testSetId() {
        client.setId(1);
        assertEquals(1, client.getId());
    }

    @Test
    void testSetLatitude() {
        client.setLatitude(40.7128);
        assertEquals(40.7128, client.getLatitude());
    }

    @Test
    void testSetLongitude() {
        client.setLongitude(-74.0060);
        assertEquals(-74.0060, client.getLongitude());
    }
}