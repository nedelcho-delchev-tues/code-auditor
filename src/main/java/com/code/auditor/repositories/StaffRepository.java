package com.code.auditor.repositories;

import com.code.auditor.domain.Staff;
import com.code.auditor.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {
    Optional<Staff> findByEmail(String email);

    List<Staff> findByRole(Role role);
}
