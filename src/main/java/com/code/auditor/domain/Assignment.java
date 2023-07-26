package com.code.auditor.domain;

import com.google.gson.annotations.Expose;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "ASSIGNMENT")
public class Assignment {

    /** The id. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "TITLE", columnDefinition = "VARCHAR", nullable = false, unique = true)
    @Expose
    private String title;

    @Column(name = "DESCRIPTION", columnDefinition = "VARCHAR", nullable = false, unique = true)
    @Expose
    private String description;

    @Column(name = "CREATE_AT", nullable = false)
    @Expose
    private Date createdTime;

    @Column(name = "MODIFIED_AT", nullable = false)
    @Expose
    private Date modifiedAT;

    @OneToOne(mappedBy = "assignment")
    private StudentSubmission submissions;

    @ManyToOne
    @JoinColumn(name = "staff_id")
    private Staff staff;

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
}