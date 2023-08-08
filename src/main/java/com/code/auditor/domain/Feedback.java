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
    private Date createdTime;

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

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    @PrePersist
    protected void prePersist() {
        if (this.createdTime == null) createdTime = new Date();
    }
}
