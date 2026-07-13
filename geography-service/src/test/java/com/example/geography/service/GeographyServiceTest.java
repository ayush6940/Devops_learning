package com.example.geography.service;

import com.example.geography.dto.LocationResponse;
import com.example.geography.dto.StateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GeographyServiceTest {

    private GeographyService geographyService;

    @BeforeEach
    void setUp() {
        geographyService = new GeographyService();
    }

    @Test
    void testGetHealthStatus() {
        Map<String, String> status = geographyService.getHealthStatus();
        assertEquals("UP", status.get("status"));
        assertEquals("geography-service", status.get("service"));
    }

    @Test
    void testGetCapitalInfo() {
        Map<String, String> response = geographyService.getCapitalInfo();
        assertEquals("France", response.get("country"));
        assertEquals("Paris", response.get("capital"));
    }

    @Test
    void testEchoCoordinates() {
        Map<String, String> payload = new HashMap<>();
        payload.put("lat", "12.9716");
        payload.put("lng", "77.5946");

        Map<String, String> response = geographyService.echoCoordinates(payload);
        assertNotNull(response.get("received_coordinates"));
        assertEquals("Coordinates received successfully", response.get("status"));
    }

    @Test
    void testMapStateToLocation_KnownState() {
        StateRequest request = new StateRequest();
        request.setRequestId("req-1");
        request.setState("Karnataka");

        LocationResponse response = geographyService.mapStateToLocation(request);

        assertEquals("req-1", response.getRequestId());
        assertEquals("Karnataka", response.getState());
        assertEquals("Bangalore", response.getCity());
        assertEquals("Whitefield", response.getArea());
        assertEquals("ITPL Main Road", response.getRoad());
    }

    @Test
    void testMapStateToLocation_UnknownState() {
        StateRequest request = new StateRequest();
        request.setRequestId("req-2");
        request.setState("Goa");

        LocationResponse response = geographyService.mapStateToLocation(request);

        assertEquals("req-2", response.getRequestId());
        assertEquals("Goa", response.getState());
        assertEquals("Unknown", response.getCity());
        assertEquals("Unknown", response.getArea());
        assertEquals("Unknown", response.getRoad());
    }

    @Test
    void testMapStateToLocation_NullState() {
        StateRequest request = new StateRequest();
        request.setRequestId("req-3");
        request.setState(null);

        LocationResponse response = geographyService.mapStateToLocation(request);

        assertEquals("req-3", response.getRequestId());
        assertEquals(null, response.getState());
        assertEquals("Unknown", response.getCity());
        assertEquals("Unknown", response.getArea());
        assertEquals("Unknown", response.getRoad());
    }
}
