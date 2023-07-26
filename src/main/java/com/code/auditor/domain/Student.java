package com.code.auditor.domain;

import com.google.gson.annotations.Expose;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "STUDENTS")
public class Student {
    /** The id. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    /**
     * The first name of the student.
     */
    @Column(name = "FIRST_NAME", columnDefinition = "VARCHAR", nullable = false)
    @Expose
    private String firstName;

    /**
     * The last name of the student.
     */
    @Column(name = "LAST_NAME", columnDefinition = "VARCHAR", nullable = false)
    @Expose
    private String lastName;

    /**
     * The email of the student.
     */
    @Column(name = "EMAIL", columnDefinition = "VARCHAR", nullable = false, unique = true)
    @Expose
    private String email;

    /**
     * The password of the student.
     */
    @Column(name = "PASSWORD", columnDefinition = "VARCHAR", nullable = false)
    private String password;

    /**
     * The faculty of the student.
     */
    @Column(name = "FACULTY", columnDefinition = "VARCHAR", nullable = false, unique = true)
    @Expose
    private String faculty;

    /**
     * The faculty number of the student.
     */
    @Column(name = "FACULTY_NUMBER", columnDefinition = "BIGINT", nullable = false, unique = true)
    @Expose
    private Long facultyNumber;

    /**
     * The email of the student.
     */
    @Column(name = "SPECIALIZATION", columnDefinition = "VARCHAR", nullable = false, unique = true)
    @Expose
    private String specialization;

    /**
     * The email of the student.
     */
    @Column(name = "STUDENT_GROUP", columnDefinition = "INTEGER", nullable = false, unique = true)
    @Expose
    private Integer group;

    /**
     * The email of the student.
     */
    @Column(name = "STUDENT_STREAM", columnDefinition = "INTEGER", nullable = false, unique = true)
    @Expose
    private Integer stream;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<StudentSubmission> submissions;
}
