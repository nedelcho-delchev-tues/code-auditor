package com.code.auditor.dtos;

import com.code.auditor.domain.Feedback;

import java.util.List;

public class StudentSubmissionDTO {
    private Long id;
    private Long userId;
    private Long assignmentId;
    private String fileName;
    private byte[] content;
    private List<Feedback> feedbacks;

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

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public List<Feedback> getFeedbacks() {
        return feedbacks;
    }

    public void setFeedbacks(List<Feedback> feedbacks) {
        this.feedbacks = feedbacks;
    }

    public StudentSubmissionDTO(Long id, Long userId, Long assignmentId, String fileName) {
        this.id = id;
        this.userId = userId;
        this.assignmentId = assignmentId;
        this.fileName = fileName;
    }

    public StudentSubmissionDTO(Long id, Long userId, Long assignmentId, String fileName, byte[] content) {
        this.id = id;
        this.userId = userId;
        this.assignmentId = assignmentId;
        this.fileName = fileName;
        this.content = content;
    }

    public StudentSubmissionDTO(Long id, Long userId, Long assignmentId, String fileName, List<Feedback> feedbacks) {
        this.id = id;
        this.userId = userId;
        this.assignmentId = assignmentId;
        this.fileName = fileName;
        this.feedbacks = feedbacks;
    }
}