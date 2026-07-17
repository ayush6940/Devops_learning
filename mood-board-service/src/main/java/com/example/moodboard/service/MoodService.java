package com.example.moodboard.service;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.HashMap;

@Service
public class MoodService {

    private final org.springframework.kafka.core.KafkaTemplate<String, Object> kafkaTemplate;

    public MoodService(org.springframework.kafka.core.KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public Map<String, String> getHealthStatus() {
        Map<String, String> status = new HashMap<>();
        status.put("status", "UP");
        status.put("service", "mood-board-service");
        return status;
    }

    public Map<String, String> generateMoodMessage() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Hello from the Mood Board Service!");
        return response;
    }

    public Map<String, String> echoMessage(Map<String, String> payload) {
        Map<String, String> response = new HashMap<>();
        response.put("received", payload.toString());
        response.put("status", "Echoed successfully");
        return response;
    }

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(MoodService.class);

    public com.example.moodboard.dto.StateRequest processStateRequest(com.example.moodboard.dto.StateRequest request) {
        if (request.getRequestId() == null || request.getRequestId().isEmpty()) {
            request.setRequestId(java.util.UUID.randomUUID().toString());
        }
        
        // Publish to Kafka topic
        kafkaTemplate.send("state-request", request.getRequestId(), request);
        logger.info("[correlationId={}] Published StateRequest to Kafka: {}", request.getRequestId(), request);
        
        return request;
    }
}
