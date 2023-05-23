package com.d2y.d2yspringapi.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.d2y.d2yspringapi.exceptions.D2YException;
import com.d2y.d2yspringapi.exceptions.InvalidRoleException;
import com.d2y.d2yspringapi.exceptions.UserNotFoundException;
import com.d2y.d2yspringapi.interfaces.UserServiceInterface;
import com.d2y.d2yspringapi.models.Role;
import com.d2y.d2yspringapi.models.User;
import com.d2y.d2yspringapi.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements UserServiceInterface {

  private final UserRepository userRepository;

  @Override
  public List<User> getAllUsers() {
    try {
      List<User> users = userRepository.findAll();

      return users;
    } catch (Exception e) {
      throw new D2YException("Gagal mengambil daftar pengguna", e);
    }
  }

  @Override
  public User getUserById(Long id) {
    User user = findUserById(id);
    return user;
  }

  @Override
  public void updateUserRole(Long id, Role role) throws InvalidRoleException {
    if (role == null) {
      throw new InvalidRoleException("Invalid Role!");
    }

    User user = findUserById(id);

    user.setRole(role);
    userRepository.save(user);
  }

  @Override
  public void deleteUser(Long id) {
    findUserById(id);
    userRepository.deleteById(id);
  }

  private User findUserById(Long id) {
    Optional<User> userExists = userRepository.findById(id);
    if (userExists.isPresent()) {
      return userExists.get();
    } else {
      throw new UserNotFoundException("User not found with id: " + id);
    }
  }

}