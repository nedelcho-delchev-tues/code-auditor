package com.code.auditor.controllers;

import com.code.auditor.domain.Staff;
import com.code.auditor.domain.Student;
import com.code.auditor.dtos.AuthenticationResponse;
import com.code.auditor.enums.Role;
import com.code.auditor.repositories.StudentRepository;
import com.code.auditor.services.AdminService;
import com.code.auditor.services.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ADMIN') && hasRole('PROFESSOR')")
public class AdminController {

    private final AdminService adminService;
    private final AuthenticationService authenticationService;
    private final StudentRepository studentRepository;

    public AdminController(AdminService adminService, AuthenticationService authenticationService, StudentRepository studentRepository) {
        this.adminService = adminService;
        this.authenticationService = authenticationService;
        this.studentRepository = studentRepository;
    }

    @GetMapping("/staff-by-role/{role}")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<List<Staff>> getAllStaffByRole(@PathVariable String role) {
        List<Staff> staff = adminService.getStaffByRole(Role.valueOf(role));
        return ResponseEntity.ok(staff);
    }

    @PostMapping("/register-staff")
    @PreAuthorize("hasAuthority('admin:create')")
    public ResponseEntity<AuthenticationResponse> registerStaff(@RequestBody Staff staff) {
        return ResponseEntity.ok(authenticationService.register(staff));
    }

    @PostMapping("/all-students")
    @PreAuthorize("hasAuthority('admin:read') && hasAuthority('professor:read')")
    public ResponseEntity<List<Student>> getAllStudents() {
        return ResponseEntity.ok(studentRepository.findAll());
    }
}
