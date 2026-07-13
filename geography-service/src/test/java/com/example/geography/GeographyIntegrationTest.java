package com.example.geography;

import com.example.geography.dto.LocationResponse;
import com.example.geography.dto.StateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GeographyIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void contextLoads() {
        // Just verify the context starts successfully
    }

    @Test
    void testGetHealthEndpoint() {
        ResponseEntity<String> response = restTemplate.getForEntity("/api/geography/health", String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testLocationFlow() {
        StateRequest request = new StateRequest();
        request.setRequestId("integration-test-req-1");
        request.setState("Karnataka");

        ResponseEntity<LocationResponse> response = restTemplate.postForEntity(
                "/api/geography/location",
                request,
                LocationResponse.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        LocationResponse body = response.getBody();
        assertEquals("integration-test-req-1", body.getRequestId());
        assertEquals("Karnataka", body.getState());
        assertEquals("Bangalore", body.getCity());
    }
}
