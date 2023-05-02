package com.d2y.d2yspringapi.interfaces;

import com.d2y.d2yspringapi.dto.AuthenticationResponse;
import com.d2y.d2yspringapi.dto.RegisterRequest;

public interface AuthenticationServiceInterface {

    AuthenticationResponse registerUser(RegisterRequest request);
}
