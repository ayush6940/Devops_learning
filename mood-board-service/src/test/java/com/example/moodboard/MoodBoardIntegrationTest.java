package com.example.moodboard;

import com.example.moodboard.dto.StateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = {
        "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}"
    }
)
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
@DirtiesContext
class MoodBoardIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void contextLoads() {
        // Ensure Spring context loads with Embedded Kafka
    }

    @Test
    void testGetHealthEndpoint() {
        ResponseEntity<String> response = restTemplate.getForEntity("/api/mood/health", String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testStateFlow() {
        StateRequest request = new StateRequest();
        request.setState("Delhi");

        ResponseEntity<StateRequest> response = restTemplate.postForEntity(
                "/api/mood/state",
                request,
                StateRequest.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        StateRequest body = response.getBody();
        assertNotNull(body.getRequestId());
        assertEquals("Delhi", body.getState());
    }
}
