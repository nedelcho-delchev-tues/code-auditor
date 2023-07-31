package com.code.auditor.controllers;

import com.code.auditor.domain.Assignment;
import com.code.auditor.domain.StudentSubmission;
import com.code.auditor.dtos.AssignmentRequest;
import com.code.auditor.dtos.MessageResponse;
import com.code.auditor.repositories.AssignmentRepository;
import com.code.auditor.services.AssignmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/assignment")
public class AssignmentController {

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

    @GetMapping("/find-by-staff/{staffId}")
    public ResponseEntity<List<Assignment>> findAllByStaff(@PathVariable Long staffId) {
        List<Assignment> assignments = assignmentRepository.findAllByUserId(staffId);
        return ResponseEntity.ok(assignments);
    }

    //TODO Test
    @PostMapping("{assignmentId}/submit-assignment")
    public ResponseEntity<Object> submitAssignment(@PathVariable Long assignmentId,
                                                   @RequestPart("file") MultipartFile content) {
        try {
            assignmentService.uploadAssignment(assignmentId, content);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new MessageResponse("ok","Задачата беше предадена успешно!"));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("server_error", "Проблем при качване на файла."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse("bad_request","Вече сте предали задача за това задание."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("server_error","Неочквана грешка."));
        }
    }

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN') || hasRole('PROFESSOR')")
    public ResponseEntity<String> createAssignment(@RequestBody AssignmentRequest assignmentRequest) {
        assignmentService.createAssignment(assignmentRequest);
        return new ResponseEntity<>(
                "Заданието беше създадено успешно !",
                HttpStatus.OK);
    }

    @PutMapping("{assignmentId}")
    @PreAuthorize("hasRole('ADMIN') || hasRole('PROFESSOR')")
    public ResponseEntity<String> updateAssignment(@PathVariable Long assignmentId, @RequestBody AssignmentRequest assignmentRequest) {
        assignmentService.updateAssignment(assignmentId, assignmentRequest);
        return new ResponseEntity<>(
                "Заданието беше обновено успешно !",
                HttpStatus.OK);
    }

    @DeleteMapping("{assignmentId}")
    @PreAuthorize("hasRole('ADMIN') || hasRole('PROFESSOR')")
    public ResponseEntity<String> deleteAssignment(@PathVariable Long assignmentId) {
        assignmentRepository.deleteById(assignmentId);
        return ResponseEntity.ok("Заданието беше изтрито успешно");
    }
}
