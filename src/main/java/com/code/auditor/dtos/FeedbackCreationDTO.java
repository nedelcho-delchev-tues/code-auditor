package com.code.auditor.dtos;

public class FeedbackCreationDTO {
    private Long studentSubmissionId;
    private String comment;

    public Long getStudentSubmissionId() {
        return studentSubmissionId;
    }

    public void setStudentSubmissionId(Long studentSubmissionId) {
        this.studentSubmissionId = studentSubmissionId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
