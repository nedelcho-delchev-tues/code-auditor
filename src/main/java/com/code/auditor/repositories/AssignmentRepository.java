package com.code.auditor.repositories;

import com.code.auditor.domain.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    @Query("SELECT a FROM Assignment a WHERE a.staff.id = :staffId")
    List<Assignment> findAllByStaffId(Long staffId);
}
