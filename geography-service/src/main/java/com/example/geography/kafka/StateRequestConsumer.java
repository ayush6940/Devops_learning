package com.example.geography.kafka;

import com.example.geography.dto.LocationResponse;
import com.example.geography.dto.StateRequest;
import com.example.geography.service.GeographyService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class StateRequestConsumer {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(StateRequestConsumer.class);

    private final GeographyService geographyService;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public StateRequestConsumer(GeographyService geographyService, KafkaTemplate<String, Object> kafkaTemplate) {
        this.geographyService = geographyService;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = new ObjectMapper();
    }

    @KafkaListener(topics = "state-request", groupId = "geography-service-group")
    public void consume(org.apache.kafka.clients.consumer.ConsumerRecord<String, Object> record) {
        try {
            Object message = record.value();
            
            // Convert LinkedHashMap (from JSON deserialization) back to POJO
            StateRequest request = objectMapper.convertValue(message, StateRequest.class);
            String correlationId = request.getRequestId();
            logger.info("[correlationId={}] Geography Service: Received StateRequest! Processing: {}", correlationId, request);

            // Process logic
            LocationResponse response = geographyService.mapStateToLocation(request);

            // Produce response
            kafkaTemplate.send("geography-response", response.getRequestId(), response);
            logger.info("[correlationId={}] Geography Service: Published LocationResponse to Kafka!", correlationId);
        } catch (Exception e) {
            logger.error("Error processing StateRequest", e);
        }
    }
}
