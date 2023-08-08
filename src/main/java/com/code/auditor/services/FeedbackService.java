package com.code.auditor.services;

import com.code.auditor.configuration.JwtService;
import com.code.auditor.domain.Feedback;
import com.code.auditor.domain.StudentSubmission;
import com.code.auditor.domain.User;
import com.code.auditor.dtos.FeedbackCreationDTO;
import com.code.auditor.repositories.FeedbackRepository;
import com.code.auditor.repositories.StudentSubmissionRepository;
import org.springframework.stereotype.Service;

@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final StudentSubmissionRepository studentSubmissionRepository;
    private final JwtService jwtService;

    public FeedbackService(FeedbackRepository feedbackRepository, StudentSubmissionRepository studentSubmissionRepository, JwtService jwtService) {
        this.feedbackRepository = feedbackRepository;
        this.studentSubmissionRepository = studentSubmissionRepository;
        this.jwtService = jwtService;
    }

    public void createFeedback(FeedbackCreationDTO feedbackCreationDTO) {
        User user = jwtService.getUserByRequest();

        String userFullName = UserService.buildNameAndTitle(user.getTitle(), user.getFirstName(), user.getLastName());
        Feedback feedback = new Feedback();

        StudentSubmission studentSubmission = studentSubmissionRepository.findById(feedbackCreationDTO.getStudentSubmissionId()).orElseThrow();
        feedback.setStudentSubmission(studentSubmission);
        feedback.setComment(feedbackCreationDTO.getComment());
        feedback.setCommenter(userFullName);

        feedbackRepository.save(feedback);
    }
}
