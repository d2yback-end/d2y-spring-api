package com.d2y.d2yspringapi.controllers;

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
import com.d2y.d2yspringapi.utils.CustomResponseConfig.CustomResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/users")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;
  private final CustomResponse response;

  @GetMapping
  public ResponseEntity<Map<String, Object>> getAllUsers() {
    List<User> users = userService.getAllUsers();
    return response.createSuccessResponse("User berhasil ditampilkan!", "users", users);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Map<String, Object>> getUserById(@PathVariable Long id) {
    User user = userService.getUserById(id);
    return response.createSuccessResponse("User berhasil ditampilkan!", "user", user);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Map<String, Object>> updateUserRole(@PathVariable Long id,
      @RequestParam("role") Role role) throws InvalidRoleException {
    try {
      userService.updateUserRole(id, role);
      return response.createSuccessResponse("User Role has been updated!");
    } catch (InvalidRoleException e) {
      return response.createErrorResponse("Invalid Role!");
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable Long id) {
    userService.deleteUser(id);
    return response.createSuccessResponse("User has been deleted!");
  }

}
