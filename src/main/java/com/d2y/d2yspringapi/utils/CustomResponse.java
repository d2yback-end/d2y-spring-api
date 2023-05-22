package com.d2y.d2yspringapi.utils;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;

public class CustomResponse {

  public static ResponseEntity<Map<String, Object>> createSuccessResponse(String message, String key, Object data) {
    Map<String, Object> responseData = new HashMap<>();
    responseData.put(key, data);
    return createResponseEntity("success", message, responseData);
  }

  public static ResponseEntity<Map<String, Object>> createSuccessResponse(String message) {
    return createResponseEntity("success", message, null);
  }

  public static ResponseEntity<Map<String, String>> createErrorResponse(String errorMessage) {
    return createResponseEntity("error", errorMessage, null);
  }

  private static <T> ResponseEntity<Map<String, T>> createResponseEntity(T status, T message, T data) {
    Map<String, T> response = new HashMap<>();
    response.put("status", status);
    response.put("message", message);
    if (data != null) {
      response.put("data", data);
    }
    return ResponseEntity.ok(response);
  }
}
