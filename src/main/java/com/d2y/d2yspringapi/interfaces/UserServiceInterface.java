package com.d2y.d2yspringapi.interfaces;

import java.util.List;
import java.util.Optional;

import com.d2y.d2yspringapi.models.User;

public interface UserServiceInterface {

    List<User> getAllUsers();

    Optional<User> getUserById(Long id);

    User updateUser(User user);

    void deleteUser(Long id);

}
