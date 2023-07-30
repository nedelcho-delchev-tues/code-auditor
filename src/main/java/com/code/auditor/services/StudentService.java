package com.code.auditor.services;

import com.code.auditor.configuration.JwtService;
import com.code.auditor.domain.Assignment;
import com.code.auditor.domain.Student;
import com.code.auditor.domain.StudentSubmission;
import com.code.auditor.repositories.AssignmentRepository;
import com.code.auditor.repositories.StudentRepository;
import com.code.auditor.repositories.StudentSubmissionRepository;
import org.springframework.stereotype.Service;

@Service
public class StudentService {
    private final StudentSubmissionRepository studentSubmissionRepository;
    private final StudentRepository studentRepository;
    private final AssignmentRepository assignmentRepository;
    private final JwtService jwtService;

    public StudentService(StudentSubmissionRepository studentSubmissionRepository, StudentRepository studentRepository, AssignmentRepository assignmentRepository, JwtService jwtService) {
        this.studentSubmissionRepository = studentSubmissionRepository;
        this.studentRepository = studentRepository;
        this.assignmentRepository = assignmentRepository;
        this.jwtService = jwtService;
    }

    public void uploadAssignment(Long assignmentId, StudentSubmission studentSubmissionRequest) {
        try {
            Student user = (Student) jwtService.extractEmailFromRequest();
            Student student = studentRepository.findByEmail(user.getEmail()).orElseThrow();
            Assignment assignment = assignmentRepository.findById(assignmentId).orElseThrow();

            String fileName = studentSubmissionRequest.getFileName();
            byte[] fileData = studentSubmissionRequest.getData();

            StudentSubmission studentSubmission = new StudentSubmission();
            studentSubmission.setStudent(student);
            studentSubmission.setFileName(fileName);
            studentSubmission.setData(fileData);
            studentSubmission.setAssignment(assignment);

            studentSubmissionRepository.save(studentSubmission);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error occurred while uploading the assignment.");
        }
    }
}
