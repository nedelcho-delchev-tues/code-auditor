package com.code.auditor.controllers;

import com.code.auditor.domain.Assignment;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/assignment")
public class AssignmentController {

    @PostMapping("/create")
    public void createAssignment(@RequestBody Assignment assignment) {
    }
}
