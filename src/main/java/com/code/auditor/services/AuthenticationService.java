package com.code.auditor.services;

import com.code.auditor.configuration.JwtService;
import com.code.auditor.domain.Token;
import com.code.auditor.domain.User;
import com.code.auditor.dtos.AuthenticationRequest;
import com.code.auditor.dtos.AuthenticationResponse;
import com.code.auditor.enums.TokenType;
import com.code.auditor.exceptions.InvalidEmailException;
import com.code.auditor.exceptions.InvalidPasswordException;
import com.code.auditor.repositories.TokenRepository;
import com.code.auditor.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
public class AuthenticationService {

    private static final int MIN_CHAR_FOR_PASSWORD = 6;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager, TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.tokenRepository = tokenRepository;
    }

    public AuthenticationResponse register(User user) {
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();

        if(!isValidEmail(user.getEmail())){
            throw new InvalidEmailException("Невалиден имейл");
        }

        if (user.getPassword().length() < MIN_CHAR_FOR_PASSWORD){
            throw new InvalidPasswordException("Невалидна парола. Паролата трябва да е минимум 6 символа");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(user, jwtToken);
        authenticationResponse.setAccessToken(jwtToken);
        authenticationResponse.setRefreshToken(refreshToken);
        return authenticationResponse;
    }

    public AuthenticationResponse authenticateUser(AuthenticationRequest request) {
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
        ));
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        authenticationResponse.setAccessToken(jwtToken);
        authenticationResponse.setRefreshToken(refreshToken);
        return authenticationResponse;
    }

    private boolean isValidEmail(String email) {
        return EmailValidator.getInstance().isValid(email);
    }

    private void saveUserToken(User user, String jwtToken) {
        Token token = new Token();
        token.setUser(user);
        token.setToken(jwtToken);
        token.setTokenType(TokenType.BEARER);
        token.setExpired(false);
        token.setRevoked(false);
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUserEmail(refreshToken);
        if (userEmail != null) {
            var user = userRepository.findByEmail(userEmail)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                AuthenticationResponse authenticationResponse = new AuthenticationResponse();
                authenticationResponse.setAccessToken(accessToken);
                authenticationResponse.setRefreshToken(refreshToken);
                new ObjectMapper().writeValue(response.getOutputStream(), authenticationResponse);
            }
        }
    }
}
