package com.code.auditor.services;

import com.code.auditor.configuration.JwtService;
import com.code.auditor.domain.Staff;
import com.code.auditor.domain.Token;
import com.code.auditor.dtos.AuthenticationResponse;
import com.code.auditor.dtos.AuthenticationRequest;
import com.code.auditor.enums.TokenType;
import com.code.auditor.repositories.StaffRepository;
import com.code.auditor.repositories.TokenRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    private final StaffRepository staffRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;

    public AuthenticationService(StaffRepository staffRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager, TokenRepository tokenRepository) {
        this.staffRepository = staffRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.tokenRepository = tokenRepository;
    }

    public AuthenticationResponse registerStaff(Staff staff){
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        staff.setPassword(passwordEncoder.encode(staff.getPassword()));
        staffRepository.save(staff);
        var jwtToken = jwtService.generateToken(staff);
        var refreshToken = jwtService.generateRefreshToken(staff);
        saveStaffToken(staff, jwtToken);
        authenticationResponse.setAccessToken(jwtToken);
        authenticationResponse.setRefreshToken(refreshToken);
        return authenticationResponse;
    }

    public AuthenticationResponse authenticateStaff(AuthenticationRequest request){
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
        ));
        var staff = staffRepository.findByEmail(request.getEmail()).orElseThrow();
        var jwtToken = jwtService.generateToken(staff);
        var refreshToken = jwtService.generateRefreshToken(staff);
        revokeAllStaffTokens(staff);
        authenticationResponse.setAccessToken(jwtToken);
        authenticationResponse.setRefreshToken(refreshToken);
        return authenticationResponse;
    }

    private void saveStaffToken(Staff staff, String jwtToken) {
        Token token = new Token();
        token.setStaff(staff);
        token.setToken(jwtToken);
        token.setTokenType(TokenType.BEARER);
        token.setExpired(false);
        token.setRevoked(false);
        tokenRepository.save(token);
    }

    private void revokeAllStaffTokens(Staff staff) {
        var validUserTokens = tokenRepository.findAllValidTokenByStaff(staff.getId());
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
        final String staffEmail;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        staffEmail = jwtService.extractStaffEmail(refreshToken);
        if (staffEmail != null) {
            var user = staffRepository.findByEmail(staffEmail)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                revokeAllStaffTokens(user);
                saveStaffToken(user, accessToken);
                AuthenticationResponse authenticationResponse = new AuthenticationResponse();
                authenticationResponse.setAccessToken(accessToken);
                authenticationResponse.setRefreshToken(refreshToken);
                new ObjectMapper().writeValue(response.getOutputStream(), authenticationResponse);
            }
        }
    }
}
