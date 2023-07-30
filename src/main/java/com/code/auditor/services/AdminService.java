package com.code.auditor.services;

import com.code.auditor.domain.Staff;
import com.code.auditor.enums.Role;
import com.code.auditor.repositories.StaffRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
    private final StaffRepository staffRepository;

    public AdminService(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    public List<Staff> getStaffByRole(Role role) {
        return staffRepository.findByRole(role);
    }

}
