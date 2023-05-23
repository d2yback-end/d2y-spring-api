package com.d2y.d2yspringapi.interfaces;

import java.util.List;

import com.d2y.d2yspringapi.models.Role;
import com.d2y.d2yspringapi.models.User;

public interface UserServiceInterface {

    List<User> getAllUsers();

    User getUserById(Long id);

    void updateUserRole(Long id, Role newRole);

    void deleteUser(Long id);

}
