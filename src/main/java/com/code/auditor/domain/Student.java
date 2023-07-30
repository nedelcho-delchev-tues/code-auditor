package com.code.auditor.domain;

import com.google.gson.annotations.Expose;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "STUDENTS")
public class Student implements UserDetails {

    /**
     * The id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    /**
     * The first name of the student.
     */
    @Column(name = "FIRST_NAME", nullable = false)
    @Expose
    private String firstName;

    /**
     * The last name of the student.
     */
    @Column(name = "LAST_NAME", nullable = false)
    @Expose
    private String lastName;

    /**
     * The email of the student.
     */
    @Column(name = "EMAIL", nullable = false, unique = true)
    @Expose
    private String email;

    /**
     * The password of the student.
     */
    @Column(name = "PASSWORD", nullable = false)
    private String password;

    /**
     * The faculty of the student.
     */
    @Column(name = "FACULTY", nullable = false)
    @Expose
    private String faculty;

    /**
     * The faculty number of the student.
     */
    @Column(name = "FACULTY_NUMBER", nullable = false)
    @Expose
    private String facultyNumber;

    /**
     * The specialization of the student.
     */
    @Column(name = "SPECIALIZATION", nullable = false)
    @Expose
    private String specialization;

    /**
     * The group of the student.
     */
    @Column(name = "STUDENT_GROUP", nullable = false)
    @Expose
    private String group;

    /**
     * The stream of the student.
     */
    @Column(name = "STUDENT_STREAM", nullable = false)
    @Expose
    private String stream;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<StudentSubmission> submissions;

    @OneToMany(mappedBy = "student")
    private List<Token> tokens;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Long getId() {
        return id;
    }

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

    public String getFacultyNumber() {
        return facultyNumber;
    }

    public void setFacultyNumber(String facultyNumber) {
        this.facultyNumber = facultyNumber;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getStream() {
        return stream;
    }

    public void setStream(String stream) {
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
