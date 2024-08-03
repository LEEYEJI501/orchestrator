package com.example.orchestrator.service;

import com.example.orchestrator.constants.KafkaConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class KafkaProducerService {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendInitialDataRequest(String marketName, String code, String timeframe) {
        try {
            Map<String, String> messageMap = Map.of(
                "marketName", marketName,
                "code", code,
                "timeframe", timeframe
            );

            String message = objectMapper.writeValueAsString(messageMap);

            kafkaTemplate.send(KafkaConstants.INITIAL_DATA_REQUEST_TOPIC, code, message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send initial data request", e);
        }
    }
}
