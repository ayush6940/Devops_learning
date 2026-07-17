package com.example.moodboard.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class GeographyResponseConsumer {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(GeographyResponseConsumer.class);

    @KafkaListener(topics = "geography-response", groupId = "mood-board-group")
    public void consume(org.apache.kafka.clients.consumer.ConsumerRecord<String, Object> record) {
        Object response = record.value();
        String correlationId = record.key();
        logger.info("[correlationId={}] Mood Board Service: Received LocationResponse! Data: {}", correlationId, response);
        logger.info("[correlationId={}] -> This data could now be persisted to a Database (e.g., PostgreSQL/MongoDB) to link the requested State with its corresponding geography details.", correlationId);
    }
}
