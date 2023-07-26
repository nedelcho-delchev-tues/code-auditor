package com.code.auditor.services;

import com.code.auditor.configuration.JwtService;
import com.code.auditor.domain.Staff;
import com.code.auditor.dtos.AuthenticationResponse;
import com.code.auditor.dtos.AuthenticationRequest;
import com.code.auditor.repositories.StaffRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final StaffRepository staffRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(StaffRepository staffRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.staffRepository = staffRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationResponse registerStaff(Staff staff){
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        staff.setPassword(passwordEncoder.encode(staff.getPassword()));
        staffRepository.save(staff);
        var jwtToken = jwtService.generateToken(staff);
        authenticationResponse.setAccessToken(jwtToken);
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
        authenticationResponse.setAccessToken(jwtToken);
        return authenticationResponse;
    }
}
