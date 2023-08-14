package com.code.auditor.services;

import com.code.auditor.configuration.JwtService;
import com.code.auditor.domain.StudentSubmission;
import com.code.auditor.domain.User;
import com.code.auditor.dtos.StudentSubmissionDTO;
import com.code.auditor.enums.Role;
import com.code.auditor.repositories.StudentSubmissionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentSubmissionService {

    private final StudentSubmissionRepository studentSubmissionRepository;
    private final JwtService jwtService;

    public StudentSubmissionService(StudentSubmissionRepository studentSubmissionRepository, JwtService jwtService) {
        this.studentSubmissionRepository = studentSubmissionRepository;
        this.jwtService = jwtService;
    }


    public List<StudentSubmissionDTO> findAllSubmissionsByUserId() {
        User user = jwtService.getUserByRequest();
        List<StudentSubmission> submissions;

        if (user.getRole() == Role.ADMIN) {
            submissions = studentSubmissionRepository.findAll();
        } else {
            submissions = studentSubmissionRepository.findAllByUserId(user.getId());
        }

        return mapSubmissionsToDTOs(submissions);
    }

    public StudentSubmissionDTO findById(Long submissionId){
        StudentSubmission studentSubmission = studentSubmissionRepository.findById(submissionId).orElseThrow();
        return mapSubmissionToDTO(studentSubmission);
    }

    public void deleteById(Long submissionId){
        studentSubmissionRepository.deleteById(submissionId);
    }

    private List<StudentSubmissionDTO> mapSubmissionsToDTOs(List<StudentSubmission> submissions) {
        return submissions.stream()
                .map(this::mapSubmissionToDTO)
                .collect(Collectors.toList());
    }

    private StudentSubmissionDTO mapSubmissionToDTO(StudentSubmission submission) {
        StudentSubmissionDTO dto = new StudentSubmissionDTO();
        dto.setId(submission.getId());
        dto.setUserId(submission.getUser().getId());
        dto.setAssignmentId(submission.getAssignment().getId());
        dto.setFileName(submission.getFileName());
        dto.setContent(submission.getContent());
        dto.setFilesPresent(submission.isFilesPresent());
        dto.setBuildPassing(submission.isBuildPassing());
        dto.setProblems(submission.getProblems());
        dto.setFeedbacks(submission.getFeedbacks());

        return dto;
    }
}