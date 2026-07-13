package com.example.moodboard.service;

import com.example.moodboard.dto.StateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

class MoodServiceTest {

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    private MoodService moodService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        moodService = new MoodService(kafkaTemplate);
    }

    @Test
    void testGetHealthStatus() {
        Map<String, String> status = moodService.getHealthStatus();
        assertEquals("UP", status.get("status"));
        assertEquals("mood-board-service", status.get("service"));
    }

    @Test
    void testGenerateMoodMessage() {
        Map<String, String> response = moodService.generateMoodMessage();
        assertEquals("Hello from the Mood Board Service!", response.get("message"));
    }

    @Test
    void testEchoMessage() {
        Map<String, String> payload = new HashMap<>();
        payload.put("mood", "happy");

        Map<String, String> response = moodService.echoMessage(payload);
        assertNotNull(response.get("received"));
        assertEquals("Echoed successfully", response.get("status"));
    }

    @Test
    void testProcessStateRequest_WithRequestId() {
        StateRequest request = new StateRequest();
        request.setRequestId("existing-id");
        request.setState("Delhi");

        StateRequest response = moodService.processStateRequest(request);

        assertEquals("existing-id", response.getRequestId());
        verify(kafkaTemplate).send(eq("state-request"), eq("existing-id"), eq(request));
    }

    @Test
    void testProcessStateRequest_WithoutRequestId() {
        StateRequest request = new StateRequest();
        request.setState("Maharashtra");

        StateRequest response = moodService.processStateRequest(request);

        assertNotNull(response.getRequestId());
        assertFalse(response.getRequestId().isEmpty());
        
        ArgumentCaptor<String> idCaptor = ArgumentCaptor.forClass(String.class);
        verify(kafkaTemplate).send(eq("state-request"), idCaptor.capture(), eq(request));
        
        assertEquals(response.getRequestId(), idCaptor.getValue());
    }
}
