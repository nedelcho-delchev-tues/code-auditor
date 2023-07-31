package com.code.auditor.services;

import com.code.auditor.configuration.JwtService;
import com.code.auditor.configuration.RabbitMQConfig;
import com.code.auditor.domain.Assignment;
import com.code.auditor.domain.StudentSubmission;
import com.code.auditor.domain.User;
import com.code.auditor.dtos.AssignmentRequest;
import com.code.auditor.repositories.AssignmentRepository;
import com.code.auditor.repositories.StudentSubmissionRepository;
import com.code.auditor.repositories.UserRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class AssignmentService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final AssignmentRepository assignmentRepository;
    private final StudentSubmissionRepository studentSubmissionRepository;
    private final RabbitTemplate rabbitTemplate;

    public AssignmentService(JwtService jwtService, UserRepository userRepository, AssignmentRepository assignmentRepository, StudentSubmissionRepository studentSubmissionRepository, RabbitTemplate rabbitTemplate) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.assignmentRepository = assignmentRepository;
        this.studentSubmissionRepository = studentSubmissionRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    public void createAssignment(AssignmentRequest assignmentCreationRequest) {
        User obj = (User) jwtService.extractEmailFromRequest();
        User user = userRepository.findByEmail(obj.getEmail()).orElseThrow();

        Assignment assignment = new Assignment(
                assignmentCreationRequest.getTitle(),
                assignmentCreationRequest.getDescription(),
                assignmentCreationRequest.getSpecialFiles(),
                user
        );

        assignmentRepository.save(assignment);
    }

    public void updateAssignment(Long assignmentId, AssignmentRequest updatedAssignment) {
        Assignment existingAssignment = assignmentRepository.findById(assignmentId).orElse(null);
        if (existingAssignment == null) {
            return;
        }
        existingAssignment.setTitle(updatedAssignment.getTitle());
        existingAssignment.setDescription(updatedAssignment.getDescription());
        existingAssignment.setSpecialFiles(updatedAssignment.getSpecialFiles());
        assignmentRepository.save(existingAssignment);
    }

    public void uploadAssignment(Long assignmentId, MultipartFile content) throws IOException {
        User obj = (User) jwtService.extractEmailFromRequest();
        User user = userRepository.findByEmail(obj.getEmail()).orElseThrow();
        Assignment assignment = assignmentRepository.findById(assignmentId).orElseThrow();

        if (hasSubmittedAssignment(user.getId(), assignmentId)) {
            throw new IllegalArgumentException("Student has already submitted an assignment for this assignment.");
        }

        StudentSubmission studentSubmission = new StudentSubmission();

        studentSubmission.setData(content.getBytes());
        studentSubmission.setFileName(content.getOriginalFilename());
        studentSubmission.setUser(user);
        studentSubmission.setAssignment(assignment);

        StudentSubmission ss = studentSubmissionRepository.save(studentSubmission);
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, "submission.saved", ss.getId());
    }

    private boolean hasSubmittedAssignment(Long userId, Long assignmentId) {
        return studentSubmissionRepository.existsByUserIdAndAssignmentId(userId, assignmentId);
    }

}
