package com.code.auditor.dtos;

import com.code.auditor.domain.Feedback;

import java.util.List;

public class StudentSubmissionDTO {
    private Long id;
    private Long userId;
    private Long assignmentId;
    private String fileName;
    private byte[] content;
    private boolean filesPresent;
    private boolean buildPassing;
    private byte[] problems;
    private List<Feedback> feedbacks;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public boolean isBuildPassing() {
        return buildPassing;
    }

    public void setBuildPassing(boolean buildPassing) {
        this.buildPassing = buildPassing;
    }

    public boolean isFilesPresent() {
        return filesPresent;
    }

    public void setFilesPresent(boolean filesPresent) {
        this.filesPresent = filesPresent;
    }

    public byte[] getProblems() {
        return problems;
    }

    public void setProblems(byte[] problems) {
        this.problems = problems;
    }

    public List<Feedback> getFeedbacks() {
        return feedbacks;
    }

    public void setFeedbacks(List<Feedback> feedbacks) {
        this.feedbacks = feedbacks;
    }

    public StudentSubmissionDTO() {
    }

    public StudentSubmissionDTO(Long id, Long userId, Long assignmentId, String fileName, byte[] content, boolean filesPresent, boolean buildPassing, byte[] problems) {
        this.id = id;
        this.userId = userId;
        this.assignmentId = assignmentId;
        this.fileName = fileName;
        this.content = content;
        this.buildPassing = buildPassing;
        this.filesPresent = filesPresent;
        this.problems = problems;
    }
}