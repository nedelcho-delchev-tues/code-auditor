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
    @Column(nullable = false, unique = true)
    @Expose
    private String email;

    /**
     * The password of the student.
     */
    @Column(nullable = false)
    private String password;

    /**
     * The faculty of the student.
     */
    @Column(nullable = false)
    @Expose
    private String faculty;

    /**
     * The faculty number of the student.
     */
    @Column(name = "FACULTY_NUMBER", columnDefinition = "BIGINT", nullable = false)
    @Expose
    private Long facultyNumber;

    /**
     * The email of the student.
     */
    @Column(name = "SPECIALIZATION", columnDefinition = "VARCHAR", nullable = false)
    @Expose
    private String specialization;

    /**
     * The email of the student.
     */
    @Column(name = "STUDENT_GROUP", columnDefinition = "INTEGER", nullable = false)
    @Expose
    private Integer group;

    /**
     * The email of the student.
     */
    @Column(name = "STUDENT_STREAM", columnDefinition = "INTEGER", nullable = false)
    @Expose
    private Integer stream;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<StudentSubmission> submissions;

    @OneToMany(mappedBy = "student")
    private List<Token> tokens;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public Long getFacultyNumber() {
        return facultyNumber;
    }

    public void setFacultyNumber(Long facultyNumber) {
        this.facultyNumber = facultyNumber;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public Integer getGroup() {
        return group;
    }

    public void setGroup(Integer group) {
        this.group = group;
    }

    public Integer getStream() {
        return stream;
    }

    public void setStream(Integer stream) {
        this.stream = stream;
    }

    public List<StudentSubmission> getSubmissions() {
        return submissions;
    }

    public void setSubmissions(List<StudentSubmission> submissions) {
        this.submissions = submissions;
    }

    public List<Token> getTokens() {
        return tokens;
    }

    public void setTokens(List<Token> tokens) {
        this.tokens = tokens;
    }
}
