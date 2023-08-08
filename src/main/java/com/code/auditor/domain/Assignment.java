package com.code.auditor.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "TITLE", columnDefinition = "VARCHAR", nullable = false)
    @Expose
    private String title;

    @Column(name = "DESCRIPTION", columnDefinition = "VARCHAR", nullable = false)
    @Expose
    private String description;

    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "SPECIAL_FILES", joinColumns = @JoinColumn(name = "assignment_id"))
    @Column(name = "SPECIAL_FILE")
    private List<String> specialFiles;

    @Column(name = "CREATE_AT", nullable = false)
    @Expose
    private Date createdTime;

    @Column(name = "MODIFIED_AT", nullable = false)
    @Expose
    private Date modifiedAT;

    @OneToMany(mappedBy = "assignment")
    private List<StudentSubmission> submissions;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"firstName", "lastName", "email", "password", "title", "role", "enabled", "accountNonExpired",
            "accountNonLocked", "credentialsNonExpired", "authorities", "username", "assignments", "faculty", "facultyNumber",
            "specialization", "group", "stream", "submissions"})
    @JsonProperty("user_id")
    private User user;

    @PrePersist
    protected void prePersist() {
        if (this.createdTime == null) createdTime = new Date();
        if (this.modifiedAT == null) modifiedAT = new Date();
    }

    @PreUpdate
    protected void preUpdate() {
        this.modifiedAT = new Date();
    }

    @PreRemove
    protected void preRemove() {
        this.modifiedAT = new Date();
    }

    public Assignment() {

    }

    public Assignment(String title, String description, List<String> specialFiles, User user) {
        this.title = title;
        this.description = description;
        this.specialFiles = specialFiles;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getSpecialFiles() {
        return specialFiles;
    }

    public void setSpecialFiles(List<String> specialFiles) {
        this.specialFiles = specialFiles;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getModifiedAT() {
        return modifiedAT;
    }

    public void setModifiedAT(Date modifiedAT) {
        this.modifiedAT = modifiedAT;
    }

    public List<StudentSubmission> getSubmissions() {
        return submissions;
    }

    public void setSubmissions(List<StudentSubmission> submissions) {
        this.submissions = submissions;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}