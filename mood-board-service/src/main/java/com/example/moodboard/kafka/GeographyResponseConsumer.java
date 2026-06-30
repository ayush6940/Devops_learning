package com.example.moodboard.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class GeographyResponseConsumer {

    @KafkaListener(topics = "geography-response", groupId = "mood-board-group")
    public void consume(org.apache.kafka.clients.consumer.ConsumerRecord<String, Object> record) {
        Object response = record.value();
        System.out.println("==================================================");
        System.out.println("Mood Board Service: Received LocationResponse!");
        System.out.println("Data: " + response.toString());
        System.out.println("-> This data could now be persisted to a Database (e.g., PostgreSQL/MongoDB)");
        System.out.println("-> to link the requested State with its corresponding geography details.");
        System.out.println("==================================================");
    }
}
