package com.d2y.d2yspringapi.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.d2y.d2yspringapi.exceptions.InvalidRoleException;
import com.d2y.d2yspringapi.models.Role;
import com.d2y.d2yspringapi.models.User;
import com.d2y.d2yspringapi.services.UserService;
// import org.apache.commons.lang3.EnumUtils;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/users")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @GetMapping
  public ResponseEntity<Map<String, Object>> getAllUsers() {
    List<User> user = userService.getAllUsers();
    Map<String, Object> responseData = new HashMap<>();
    responseData.put("users", user);

    Map<String, Object> response = createSuccessResponse("User berhasil ditampilkan!", responseData);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Map<String, Object>> getUserById(@PathVariable Integer id) {
    User user = userService.getUserById(id);
    Map<String, Object> responseData = new HashMap<>();
    responseData.put("user", user);

    Map<String, Object> response = createSuccessResponse("User berhasil ditampilkan!", responseData);
    return ResponseEntity.ok(response);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Map<String, String>> updateUserRole(@PathVariable Integer id,
      @RequestParam("role") Role role) {
    try {
      userService.updateUserRole(id, role);
      Map<String, String> response = createSuccessResponse("User Role has been updated!");
      return ResponseEntity.ok(response);
    } catch (InvalidRoleException e) {
      Map<String, String> response = createErrorResponse("Invalid Role!");
      return ResponseEntity.badRequest().body(response);
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Map<String, String>> deleteUser(@PathVariable Integer id) {
    userService.deleteUser(id);

    Map<String, String> response = createSuccessResponse("User has been deleted!");
    return ResponseEntity.ok(response);
  }

  private Map<String, String> createSuccessResponse(String message) {
    Map<String, String> response = new HashMap<>();
    response.put("status", "success");
    response.put("message", message);

    return response;
  }

  private Map<String, String> createErrorResponse(String errorMessage) {
    Map<String, String> errorResponse = new HashMap<>();
    errorResponse.put("status", "error");
    errorResponse.put("message", errorMessage);
    return errorResponse;
  }

  private Map<String, Object> createSuccessResponse(String message, Map<String, Object> responseData) {
    Map<String, Object> response = new HashMap<>();
    response.put("status", "success");
    response.put("message", message);
    response.put("data", responseData);
    return response;
  }
}
