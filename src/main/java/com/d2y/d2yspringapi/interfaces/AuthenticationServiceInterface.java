package com.d2y.d2yspringapi.interfaces;

import com.d2y.d2yspringapi.dto.RegisterRequest;
import com.d2y.d2yspringapi.models.User;

public interface AuthenticationServiceInterface {

    User registerUser(RegisterRequest request);
}
