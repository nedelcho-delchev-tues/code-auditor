package com.code.auditor.domain;

import javax.persistence.*;

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

    @OneToOne
    @JoinColumn(name = "assignment_id")
    private Assignment assignment;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;
}
