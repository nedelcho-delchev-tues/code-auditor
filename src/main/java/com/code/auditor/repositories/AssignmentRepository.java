package com.code.auditor.repositories;

import com.code.auditor.domain.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    @Query("SELECT a FROM Assignment a WHERE a.user.id = :userId")
    List<Assignment> findAllByUserId(Long userId);

    @Query("SELECT a FROM Assignment a WHERE a.title LIKE %:keyword% OR a.user.firstName LIKE %:keyword% OR a.user.lastName LIKE %:keyword%")
    List<Assignment> findByTitleOrUserFirstNameOrUserLastName(String keyword);
}