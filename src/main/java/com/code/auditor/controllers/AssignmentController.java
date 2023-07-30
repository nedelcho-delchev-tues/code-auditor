package com.code.auditor.controllers;

import com.code.auditor.domain.Assignment;
import com.code.auditor.dtos.AssignmentRequest;
import com.code.auditor.repositories.AssignmentRepository;
import com.code.auditor.services.AssignmentService;
import org.springframework.expression.spel.ast.Assign;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<List<Assignment>> findAllAssignments() {
        List<Assignment> assignments = assignmentRepository.findAll();
        return ResponseEntity.ok(assignments);
    }

    @GetMapping("/staff/{staffId}")
    public ResponseEntity<List<Assignment>> findAllByStaff(@PathVariable Long staffId) {
        List<Assignment> assignments = assignmentRepository.findAllByStaffId(staffId);
        return ResponseEntity.ok(assignments);
    }

    @PostMapping()
    public ResponseEntity<String> createAssignment(@RequestBody AssignmentRequest assignmentRequest) {
        assignmentService.createAssignment(assignmentRequest);
        return new ResponseEntity<>(
                "Заданието беше създадено успешно !",
                HttpStatus.OK);
    }

    @PutMapping("{assignmentId}")
    public ResponseEntity<String> updateAssignment(@PathVariable Long assignmentId, @RequestBody AssignmentRequest assignmentRequest) {
        assignmentService.updateAssignment(assignmentId, assignmentRequest);
        return new ResponseEntity<>(
                "Заданието беше обновено успешно !",
                HttpStatus.OK);
    }

    @DeleteMapping("{assignmentId}")
    public ResponseEntity<String> deleteAssignment(@PathVariable Long assignmentId) {
        assignmentRepository.deleteById(assignmentId);
        return ResponseEntity.ok("Заданието беше изтрито успешно");
    }
}
