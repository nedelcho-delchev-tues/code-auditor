package com.code.auditor.dtos;

public class StudentSubmissionDTO {
    private Long id;
    private String fileName;
    private Long userId;
    private Long assignmentId;

    public Long getId() {
        return id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(Long assignmentId) {
        this.assignmentId = assignmentId;
    }

    public StudentSubmissionDTO(Long id, String fileName) {
        this.id = id;
        this.fileName = fileName;
    }

    public StudentSubmissionDTO(Long id, String fileName, Long userId, Long assignmentId) {
        this.id = id;
        this.fileName = fileName;
        this.userId = userId;
        this.assignmentId = assignmentId;
    }
}
