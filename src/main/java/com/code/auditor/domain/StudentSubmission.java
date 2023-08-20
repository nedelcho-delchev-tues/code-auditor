package com.code.auditor.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "STUDENT_SUBMISSIONS")
public class StudentSubmission {

    /**
     * The id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "FILE_NAME")
    private String fileName;

    @Lob
    private byte[] content;

    @Column(name = "FILES_PRESENT")
    boolean filesPresent;

    @Column(name = "BUILD_PASSING")
    boolean buildPassing;

    @Lob
    private byte[] problems;

    @ManyToOne
    @JoinColumn(name = "assignment_id")
    @JsonIgnore
    private Assignment assignment;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "studentSubmission", cascade = CascadeType.ALL, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Feedback> feedbacks;

    public StudentSubmission() {

    }

    public Long getId() {
        return id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getData() {
        return content;
    }

    public void setData(byte[] data) {
        this.content = data;
    }

    public boolean isFilesPresent() {
        return filesPresent;
    }

    public void setFilesPresent(boolean filesPresent) {
        this.filesPresent = filesPresent;
    }

    public boolean isBuildPassing() {
        return buildPassing;
    }

    public void setBuildPassing(boolean buildPassing) {
        this.buildPassing = buildPassing;
    }

    public byte[] getProblems() {
        return problems;
    }

    public void setProblems(byte[] problems) {
        this.problems = problems;
    }

    public Assignment getAssignment() {
        return assignment;
    }

    public void setAssignment(Assignment assignment) {
        this.assignment = assignment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    @Override
    public String toString() {
        return "StudentSubmission{" +
                "id=" + id +
                ", fileName='" + fileName + '\'' +
                ", assignment=" + assignment +
                ", user=" + user +
                '}';
    }
}
