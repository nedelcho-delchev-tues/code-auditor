package com.code.auditor.repositories;

import com.code.auditor.dtos.StudentSubmissionDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.code.auditor.domain.StudentSubmission;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface StudentSubmissionRepository extends JpaRepository<StudentSubmission, Long> {
    boolean existsByUserIdAndAssignmentId(Long userId, Long assignmentId);

    @Query(value = """
            select new com.code.auditor.dtos.StudentSubmissionDTO(ss.id, ss.user.id, ss.assignment.id, ss.fileName)
            from StudentSubmission ss
            inner join User u
                on ss.user.id = u.id
            inner join Assignment a
                on ss.assignment.id = a.id
            where u.id = :userId and a.id = :assignmentId
            """)
    Optional<StudentSubmissionDTO> getByUserIdAndAssignmentId(Long userId, Long assignmentId);

    @Transactional
    @Modifying
    @Query("DELETE FROM StudentSubmission ss WHERE ss.user.id = :userId AND ss.assignment.id = :assignmentId")
    void deleteByUserIdAndAssignmentId(Long userId, Long assignmentId);
}
