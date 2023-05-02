package com.d2y.d2yspringapi.interfaces;

import java.util.List;

import com.d2y.d2yspringapi.dto.RegisterRequest;
import com.d2y.d2yspringapi.models.User;

public interface UserServiceInterface {
    List<User> getUsers();

    User registerUser(RegisterRequest request);
}
