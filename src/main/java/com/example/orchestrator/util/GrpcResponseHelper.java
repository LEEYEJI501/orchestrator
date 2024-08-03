package com.example.orchestrator.util;

import com.example.grpc.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class GrpcResponseHelper {
    private final ObjectMapper objectMapper;

    public GrpcResponseHelper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public ResponseEntity<String> createJsonResponse(Response response) {
        try {
            Object resultObject = objectMapper.readValue(response.getResult(), Object.class);
            String jsonResponse = objectMapper.writeValueAsString(resultObject);
            return ResponseEntity.ok(jsonResponse);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("{\"error\": \"Failed to process response\"}");
        }
    }
}
