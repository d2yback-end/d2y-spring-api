package com.d2y.d2yspringapi.services;

import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.d2y.d2yspringapi.dto.AuthenticationRequest;
import com.d2y.d2yspringapi.dto.AuthenticationResponse;
import com.d2y.d2yspringapi.dto.RegisterRequest;
import com.d2y.d2yspringapi.exceptions.D2YException;
import com.d2y.d2yspringapi.exceptions.UserAlreadyExistsException;
import com.d2y.d2yspringapi.interfaces.AuthenticationServiceInterface;
import com.d2y.d2yspringapi.models.Role;
import com.d2y.d2yspringapi.models.Token;
import com.d2y.d2yspringapi.models.TokenType;
import com.d2y.d2yspringapi.models.User;
import com.d2y.d2yspringapi.repositories.TokenRepository;
import com.d2y.d2yspringapi.repositories.UserRepository;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements AuthenticationServiceInterface {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    /**
     * Create a new users
     * 
     * @param RegisterRequest request
     * @return User
     */
    @Override
    public User registerUser(RegisterRequest request) {
        validateUserDoesNotExist(request.getEmail());
        User user = createUserFromRequest(request);
        User savedUser = userRepository.save(user);

        return savedUser;
    }

    /**
     * Save user token to database
     * 
     * @param User   user
     * @param String jwtToken
     * @return void
     */
    @Override
    public void saveUserToken(User user, String jwtToken) {
        Token token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();

        tokenRepository.save(token);
    }

    /**
     * Validation token to verified account
     * 
     * @param String theToken
     * @return String
     */
    @Override
    public String validateToken(String theToken) {
        Token token = tokenRepository.findByToken(theToken).orElseThrow();
        if (token == null) {
            return "Invalid verification token";
        }
        User user = token.getUser();
        if (token.isExpired()) {
            tokenRepository.delete(token);
            return "Token already expired";
        }
        user.setEnabled(true);
        userRepository.save(user);
        tokenRepository.delete(token);
        return "valid";
    }

    /**
     * Login user and send the access token and refresh token
     * 
     * @param AuthenticationRequest request
     * @return AuthenticationResponse
     */
    @Override
    public AuthenticationResponse loginUser(AuthenticationRequest request) {
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();

        if (!(user.isEnabled())) {
            throw new D2YException("Please check your email, to verify your account!");
        }

        Authentication authenticate = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authenticate);

        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    /**
     * Revoke all user tokens
     * 
     * @param User user
     * @return void
     */
    @Override
    public void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    /**
     * Generate refresh token
     * 
     * @param HttpServletRequest  request
     * @param HttpServletResponse response
     * @return void
     */
    @Override
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException, StreamWriteException, DatabindException, java.io.IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }

        refreshToken = authHeader.split(" ")[1].trim();
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            var user = userRepository.findByEmail(userEmail)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();

                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

    /**
     * Get the current user login
     * 
     * @return User
     */
    @Override
    @Transactional(readOnly = true)
    public User getCurrentUser() {
        Jwt principal = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findByEmail(principal.getSubject())
                .orElseThrow(() -> new UsernameNotFoundException("User name not found - " + principal.getSubject()));
    }

    /**
     * Check if user has been login
     * 
     * @return boolean
     */
    @Override
    public boolean isLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
    }

    private void validateUserDoesNotExist(String email) {
        Optional<User> existUser = userRepository.findByEmail(email);
        if (existUser.isPresent()) {
            throw new UserAlreadyExistsException(
                    "User with email " + email + " already exists");
        }
    }

    private User createUserFromRequest(RegisterRequest request) {
        return User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
    }

}