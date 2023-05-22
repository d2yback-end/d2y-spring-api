package com.d2y.d2yspringapi.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;

@Configuration
public class CustomResponseConfig {

    @Bean
    public CustomResponse customResponse() {
        return new CustomResponse();
    }

    public static class CustomResponse {

        public ResponseEntity<Map<String, Object>> createSuccessResponse(String message, String key, Object data) {
            Map<String, Object> responseData = new HashMap<>();
            responseData.put(key, data);
            return createResponseEntity("success", message, responseData);
        }

        public ResponseEntity<Map<String, Object>> createSuccessResponse(String message) {
            return createResponseEntity("success", message, null);
        }

        public ResponseEntity<Map<String, Object>> createErrorResponse(String errorMessage) {
            return createResponseEntity("error", errorMessage, null);
        }

        private <T> ResponseEntity<Map<String, T>> createResponseEntity(T status, T message, T data) {
            Map<String, T> response = new HashMap<>();
            response.put("status", status);
            response.put("message", message);
            if (data != null) {
                response.put("data", data);
            }
            return ResponseEntity.ok(response);
        }
    }
}