package com.code.auditor.services;

import com.code.auditor.configuration.JwtService;
import com.code.auditor.domain.Assignment;
import com.code.auditor.domain.Feedback;
import com.code.auditor.domain.StudentSubmission;
import com.code.auditor.domain.User;
import com.code.auditor.dtos.AssignmentRequestDTO;
import com.code.auditor.dtos.StudentSubmissionDTO;
import com.code.auditor.exceptions.SubmissionSubmittedException;
import com.code.auditor.repositories.AssignmentRepository;
import com.code.auditor.repositories.FeedbackRepository;
import com.code.auditor.repositories.StudentSubmissionRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class AssignmentService {

    private final JwtService jwtService;
    private final AssignmentRepository assignmentRepository;
    private final StudentSubmissionRepository studentSubmissionRepository;
    private final FeedbackRepository feedbackRepository;
    private final RabbitTemplate rabbitTemplate;

    public AssignmentService(JwtService jwtService, AssignmentRepository assignmentRepository,
                             StudentSubmissionRepository studentSubmissionRepository, FeedbackRepository feedbackRepository, RabbitTemplate rabbitTemplate) {
        this.jwtService = jwtService;
        this.assignmentRepository = assignmentRepository;
        this.studentSubmissionRepository = studentSubmissionRepository;
        this.feedbackRepository = feedbackRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    public void createAssignment(AssignmentRequestDTO assignmentCreationRequest) {
        User user = jwtService.getUserByRequest();

        Assignment assignment = new Assignment(
                assignmentCreationRequest.getTitle(),
                assignmentCreationRequest.getDescription(),
                assignmentCreationRequest.getSpecialFiles(),
                user
        );

        assignmentRepository.save(assignment);
    }

    public void updateAssignment(Long assignmentId, AssignmentRequestDTO updatedAssignment) {
        Assignment existingAssignment = assignmentRepository.findById(assignmentId).orElse(null);
        if (existingAssignment == null) {
            return;
        }
        existingAssignment.setTitle(updatedAssignment.getTitle());
        existingAssignment.setDescription(updatedAssignment.getDescription());
        existingAssignment.setSpecialFiles(updatedAssignment.getSpecialFiles());
        assignmentRepository.save(existingAssignment);
    }

    public void submitAssignment(Long assignmentId, MultipartFile content) throws IOException {
        User user = jwtService.getUserByRequest();
        Assignment assignment = assignmentRepository.findById(assignmentId).orElseThrow();
        if (hasSubmittedAssignment(user.getId(), assignmentId)) {
            throw new SubmissionSubmittedException("Вече сте предали решение за тази задача.");
        }

        StudentSubmission studentSubmission = new StudentSubmission();

        studentSubmission.setData(content.getBytes());
        studentSubmission.setFileName(content.getOriginalFilename());
        studentSubmission.setUser(user);
        studentSubmission.setAssignment(assignment);

        StudentSubmission ss = studentSubmissionRepository.save(studentSubmission);
        rabbitTemplate.convertAndSend("code_auditor_exchange", "submission.saved", ss.getId());
    }

    @Transactional
    public StudentSubmissionDTO getStudentSubmissionByAssignment(Long assignmentId) {
        User user = jwtService.getUserByRequest();
        StudentSubmissionDTO studentSubmissionDTO = studentSubmissionRepository.getByUserIdAndAssignmentId(user.getId(), assignmentId).orElseThrow(NoSuchElementException::new);
        List<Feedback> feedbacks = feedbackRepository.findByStudentSubmissionId(studentSubmissionDTO.getId());
        studentSubmissionDTO.setFeedbacks(feedbacks);
        return studentSubmissionDTO;
    }

    public void deleteSubmissionByStudent(Long assignmentId){
        User user = jwtService.getUserByRequest();
        studentSubmissionRepository.deleteByUserIdAndAssignmentId(assignmentId, user.getId());
    }

    private boolean hasSubmittedAssignment(Long userId, Long assignmentId) {
        return studentSubmissionRepository.existsByUserIdAndAssignmentId(userId, assignmentId);
    }
}
