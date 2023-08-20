package com.code.auditor.controllers;

import com.code.auditor.domain.Assignment;
import com.code.auditor.dtos.AssignmentRequestDTO;
import com.code.auditor.dtos.MessageResponse;
import com.code.auditor.dtos.StudentSubmissionDTO;
import com.code.auditor.repositories.AssignmentRepository;
import com.code.auditor.services.AssignmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v1/assignment")
public class AssignmentController {

    private static final Logger logger = LoggerFactory.getLogger(AssignmentController.class);

    private final AssignmentService assignmentService;
    private final AssignmentRepository assignmentRepository;

    public AssignmentController(AssignmentService assignmentService, AssignmentRepository assignmentRepository) {
        this.assignmentService = assignmentService;
        this.assignmentRepository = assignmentRepository;
    }

    @GetMapping()
    public ResponseEntity<List<Assignment>> getAllAssignments() {
        List<Assignment> assignments = assignmentRepository.findAll();
        return ResponseEntity.ok(assignments);
    }

    @GetMapping("/{assignmentId}")
    public ResponseEntity<Assignment> getAssignmentById(@PathVariable Long assignmentId) {
        Assignment assignment = assignmentRepository.findById(assignmentId).orElseThrow();
        return ResponseEntity.ok(assignment);
    }

    @GetMapping("{assignmentId}/get_submission")
    public ResponseEntity<Object> getStudentSubmission(@PathVariable Long assignmentId) {
        try {
            StudentSubmissionDTO studentSubmission = assignmentService.getStudentSubmissionByAssignment(assignmentId);
            return ResponseEntity.ok().body(studentSubmission);
        } catch (NoSuchElementException e) {
            logger.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new MessageResponse(HttpStatus.NOT_FOUND.value(), "Не е намерана задача отговаряща на изискванията."));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new MessageResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Неочаквана грешка"));
        }
    }

    @PostMapping("{assignmentId}/submit-assignment")
    public ResponseEntity<Object> submitAssignment(@PathVariable Long assignmentId,
                                                   @RequestPart("file") MultipartFile content) throws IOException {
            assignmentService.submitAssignment(assignmentId, content);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new MessageResponse(HttpStatus.OK.value(), "Задачата беше предадена успешно!"));

    }

    @DeleteMapping("{assignmentId}/delete_submission")
    public ResponseEntity<Object> deleteStudentSubmission(@PathVariable Long assignmentId) {
        assignmentService.deleteSubmissionByUserAndAssignment(assignmentId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new MessageResponse(HttpStatus.OK.value(), "Задачата беше изтрита успешно."));
    }

    @GetMapping("/find-by-staff/{staffId}")
    public ResponseEntity<List<Assignment>> findAllByStaff(@PathVariable Long staffId) {
        List<Assignment> assignments = assignmentRepository.findAllByUserId(staffId);
        return ResponseEntity.ok(assignments);
    }

    @GetMapping("/count_assignments")
    public ResponseEntity<Object> countAllAssignments(){
        return ResponseEntity.ok(assignmentRepository.count());
    }

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN') || hasRole('PROFESSOR')")
    public ResponseEntity<Object> createAssignment(@RequestBody AssignmentRequestDTO assignmentRequest) {
        assignmentService.createAssignment(assignmentRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new MessageResponse(HttpStatus.OK.value(), "Заданието беше създадено успешно."));
    }

    @PutMapping("{assignmentId}")
    @PreAuthorize("hasRole('ADMIN') || hasRole('PROFESSOR')")
    public ResponseEntity<Object> updateAssignment(@PathVariable Long assignmentId, @RequestBody AssignmentRequestDTO assignmentRequest) {
        assignmentService.updateAssignment(assignmentId, assignmentRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new MessageResponse(HttpStatus.OK.value(), "Заданието беше обновено успешно."));
    }

    @DeleteMapping("{assignmentId}")
    @PreAuthorize("hasRole('ADMIN') || hasRole('PROFESSOR')")
    public ResponseEntity<Object> deleteAssignment(@PathVariable Long assignmentId) {
        assignmentRepository.deleteById(assignmentId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new MessageResponse(HttpStatus.OK.value(), "Заданието беше изтрито успешно."));
    }
}
