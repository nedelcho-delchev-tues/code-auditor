package com.code.auditor.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.code.auditor.domain.StudentSubmission;

@Repository
public interface StudentSubmissionRepository extends JpaRepository<StudentSubmission, Long> {
    boolean existsByUserIdAndAssignmentId(Long userId, Long assignmentId);
}
