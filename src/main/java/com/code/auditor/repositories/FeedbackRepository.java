package com.code.auditor.repositories;

import com.code.auditor.domain.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    @Transactional
    List<Feedback> findByStudentSubmissionId(Long studentSubmissionId);
}
