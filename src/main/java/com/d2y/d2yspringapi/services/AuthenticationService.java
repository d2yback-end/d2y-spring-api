package com.d2y.d2yspringapi.services;

import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.d2y.d2yspringapi.dto.AuthenticationResponse;
import com.d2y.d2yspringapi.dto.RegisterRequest;
import com.d2y.d2yspringapi.exceptions.UserAlreadyExistsException;
import com.d2y.d2yspringapi.interfaces.AuthenticationServiceInterface;
import com.d2y.d2yspringapi.models.Token;
import com.d2y.d2yspringapi.models.TokenType;
import com.d2y.d2yspringapi.models.User;
import com.d2y.d2yspringapi.repositories.TokenRepository;
import com.d2y.d2yspringapi.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements AuthenticationServiceInterface {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtServie;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthenticationResponse registerUser(RegisterRequest request) {
        Optional<User> existUser = userRepository.findByEmail(request.getEmail());
        if (existUser.isPresent()) {
            throw new UserAlreadyExistsException(
                    "User with email " + request.getEmail() + " already exists");
        }

        User user = User.builder()
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();

        User savedUser = userRepository.save(user);
        String jwtToken = jwtServie.generateToken(user);
        String refreshToken = jwtServie.generateRefreshToken(user);

        saveUserToken(savedUser, jwtToken);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void saveUserToken(User user, String jwtToken) {
        Token token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();

        tokenRepository.save(token);
    }

}