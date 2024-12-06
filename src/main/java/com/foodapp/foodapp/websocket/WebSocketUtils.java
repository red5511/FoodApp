package com.foodapp.foodapp.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class WebSocketUtils {
    public static String extractCompanyIdFromMessage(String message) {
        // Assuming the message is JSON, parse it
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode rootNode = objectMapper.readTree(message);
            return rootNode.get("companyId").asText();
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse message", e);
        }
    }
}
