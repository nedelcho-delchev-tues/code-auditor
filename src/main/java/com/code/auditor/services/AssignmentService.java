package com.code.auditor.services;

import com.code.auditor.configuration.JwtService;
import com.code.auditor.domain.Assignment;
import com.code.auditor.domain.Staff;
import com.code.auditor.dtos.AssignmentRequest;
import com.code.auditor.repositories.AssignmentRepository;
import com.code.auditor.repositories.StaffRepository;
import org.springframework.stereotype.Service;

@Service
public class AssignmentService {

    private final JwtService jwtService;
    private final StaffRepository staffRepository;
    private final AssignmentRepository assignmentRepository;

    public AssignmentService(JwtService jwtService, StaffRepository staffRepository, AssignmentRepository assignmentRepository) {
        this.jwtService = jwtService;
        this.staffRepository = staffRepository;
        this.assignmentRepository = assignmentRepository;
    }

    public void createAssignment(AssignmentRequest assignmentCreationRequest) {
        String email = jwtService.extractEmailFromRequest();
        Staff staff = staffRepository.findByEmail(email).orElseThrow();

        Assignment assignment = new Assignment(
                assignmentCreationRequest.getTitle(),
                assignmentCreationRequest.getDescription(),
                assignmentCreationRequest.getSpecialFiles(),
                staff
        );

        assignmentRepository.save(assignment);
    }

    public void updateAssignment(Long assignmentId, AssignmentRequest updatedAssignment){
        Assignment existingAssignment = assignmentRepository.findById(assignmentId).orElse(null);
        if (existingAssignment == null) {
            return;
        }
        existingAssignment.setTitle(updatedAssignment.getTitle());
        existingAssignment.setDescription(updatedAssignment.getDescription());
        existingAssignment.setSpecialFiles(updatedAssignment.getSpecialFiles());
        assignmentRepository.save(existingAssignment);
    }

}
