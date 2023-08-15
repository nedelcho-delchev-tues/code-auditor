package com.code.auditor.dtos;

public class FeedbackCreationDTO {
    private Long studentSubmissionId;
    private String comment;
    private String commenter;

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

    public String getCommenter() {
        return commenter;
    }

    public void setCommenter(String commenter) {
        this.commenter = commenter;
    }
}
