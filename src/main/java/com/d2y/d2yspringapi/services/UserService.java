package com.d2y.d2yspringapi.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

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
    return userRepository.findAll();
  }

  @Override
  public User getUserById(Integer id) {
    User user = findUserById(id);
    return user;
  }

  @Override
  public void updateUserRole(Integer id, Role newRole) {
    User user = findUserById(id);

    user.setRole(newRole);
    userRepository.save(user);
  }

  @Override
  public void deleteUser(Integer id) {
    findUserById(id);
    userRepository.deleteById(id);
  }

  private User findUserById(Integer id) {
    Optional<User> userExists = userRepository.findById(id);
    if (userExists.isPresent()) {
      return userExists.get();
    } else {
      throw new UserNotFoundException("User not found with id: " + id);
    }
  }

}