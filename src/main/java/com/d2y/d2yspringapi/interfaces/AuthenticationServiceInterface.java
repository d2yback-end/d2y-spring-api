package com.d2y.d2yspringapi.interfaces;

import com.d2y.d2yspringapi.dto.AuthenticationRequest;
import com.d2y.d2yspringapi.dto.AuthenticationResponse;
import com.d2y.d2yspringapi.dto.RegisterRequest;
import com.d2y.d2yspringapi.models.User;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthenticationServiceInterface {

    User registerUser(RegisterRequest request);

    void saveUserToken(User user, String jwtToken);

    String validateToken(String theToken);

    AuthenticationResponse loginUser(AuthenticationRequest request);

    void revokeAllUserTokens(User user);

    void refreshToken(HttpServletRequest request, HttpServletResponse response)
            throws IOException, StreamWriteException, DatabindException, java.io.IOException;

    User getCurrentUser();

    boolean isLoggedIn();
}
