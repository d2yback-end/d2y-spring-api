package com.d2y.d2yspringapi.controllers;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.d2y.d2yspringapi.dto.RegisterRequest;
import com.d2y.d2yspringapi.helpers.RegistrationCompleteEvent;
import com.d2y.d2yspringapi.models.User;
import com.d2y.d2yspringapi.repositories.TokenRepository;
import com.d2y.d2yspringapi.services.AuthenticationService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final TokenRepository tokenRepository;
    private final ApplicationEventPublisher publisher;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest,
            final HttpServletRequest request) {
        User user = authenticationService.registerUser(registerRequest);
        publisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl(request)));
        return ResponseEntity.ok("Success!  Please, check your email for to complete your registration");
    }

    public String applicationUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }
}
