package com.d2y.d2yspringapi.interfaces;

import java.util.List;

import com.d2y.d2yspringapi.models.Role;
import com.d2y.d2yspringapi.models.User;

public interface UserServiceInterface {

    List<User> getAllUsers();

    User getUserById(Integer id);

    void updateUserRole(Integer id, Role newRole);

    void deleteUser(Integer id);

}
