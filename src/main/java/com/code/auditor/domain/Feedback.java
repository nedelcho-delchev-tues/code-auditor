package com.code.auditor.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_submission_id", nullable = false)
    @JsonIgnore
    private StudentSubmission studentSubmission;

    @Column(nullable = false)
    private String comment;

    @Column(nullable = false)
    private String commenter;

    @Column(name = "CREATE_AT", nullable = false)
    @Expose
    private Date createdAt;

    public Long getId() {
        return id;
    }

    public StudentSubmission getStudentSubmission() {
        return studentSubmission;
    }

    public void setStudentSubmission(StudentSubmission studentSubmission) {
        this.studentSubmission = studentSubmission;
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdTime) {
        this.createdAt = createdTime;
    }

    @PrePersist
    protected void prePersist() {
        if (this.createdAt == null) createdAt = new Date();
    }
}
