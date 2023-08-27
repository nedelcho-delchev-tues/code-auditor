package com.code.auditor.dtos;

import com.code.auditor.domain.Assignment;
import com.code.auditor.domain.Feedback;
import com.code.auditor.domain.User;

import java.util.List;

public class StudentSubmissionDTO {
    private Long id;
    private User user;
    private Assignment assignment;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Assignment getAssignment() {
        return assignment;
    }

    public void setAssignment(Assignment assignment) {
        this.assignment = assignment;
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

    public StudentSubmissionDTO(Long id, User user, Assignment assignment, String fileName, byte[] content, boolean filesPresent, boolean buildPassing, byte[] problems) {
        this.id = id;
        this.user = user;
        this.assignment = assignment;
        this.fileName = fileName;
        this.content = content;
        this.filesPresent = filesPresent;
        this.buildPassing = buildPassing;
        this.problems = problems;
    }
}