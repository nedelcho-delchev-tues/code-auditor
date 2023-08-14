package com.code.auditor.controllers;

import com.code.auditor.dtos.MessageResponse;
import com.code.auditor.dtos.StudentSubmissionDTO;
import com.code.auditor.services.StudentSubmissionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/submission")
public class SubmissionController {
    private final StudentSubmissionService studentSubmissionService;

    public SubmissionController(StudentSubmissionService studentSubmissionService) {
        this.studentSubmissionService = studentSubmissionService;
    }

    @GetMapping()
    public ResponseEntity<Object> getAllSubmissions(){
        List<StudentSubmissionDTO> studentSubmissions = studentSubmissionService.findAllSubmissionsByUserId();
        return ResponseEntity.ok(studentSubmissions);
    }

    @GetMapping("/{submissionId}")
    public ResponseEntity<Object> getSubmissionById(@PathVariable Long submissionId){
        StudentSubmissionDTO studentSubmission = studentSubmissionService.findById(submissionId);
        return ResponseEntity.ok(studentSubmission);
    }

    @DeleteMapping("/{submissionId}")
    public ResponseEntity<Object> deleteSubmissionById(@PathVariable Long submissionId){
        studentSubmissionService.deleteById(submissionId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new MessageResponse(HttpStatus.OK.value(), "Задачата беше изтрита успешно."));
    }
}
