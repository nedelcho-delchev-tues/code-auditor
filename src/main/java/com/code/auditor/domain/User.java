package com.code.auditor.domain;

import com.code.auditor.enums.Role;
import com.code.auditor.validation.ValidEmail;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "\"USER\"")
public class User implements UserDetails {

    /**
     * The id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    /**
     * The first name of the user.
     */
    @Column(name = "FIRST_NAME", columnDefinition = "VARCHAR", nullable = false)
    @Expose
    private String firstName;

    /**
     * The last name of the user.
     */
    @Column(name = "LAST_NAME", columnDefinition = "VARCHAR", nullable = false)
    @Expose
    private String lastName;

    /**
     * The email of the user.
     */
    @Column(nullable = false, unique = true)
    @Expose
    @ValidEmail
    private String email;

    /**
     * The password of the user.
     */
    @Column(nullable = false)
    @Size(min = 6, message = "Паролата трябва да е поне дълга поне 6 знака")
    private String password;

    @Enumerated(EnumType.STRING)
    @Expose
    @Column(name = "ROLE", columnDefinition = "VARCHAR", nullable = false)
    private Role role;

    /**
     * The title of the user.
     */
    @Expose
    private String title;

    /**
     * The faculty of the student.
     */
    @Column(name = "FACULTY")
    @Expose
    private String faculty;

    /**
     * The faculty number of the student.
     */
    @Column(name = "FACULTY_NUMBER")
    @Expose
    private String facultyNumber;

    /**
     * The specialization of the student.
     */
    @Column(name = "SPECIALIZATION")
    @Expose
    private String specialization;

    /**
     * The group of the student.
     */
    @Column(name = "STUDENT_GROUP")
    @Expose
    private String group;

    /**
     * The stream of the student.
     */
    @Column(name = "STUDENT_STREAM")
    @Expose
    private String stream;

    @Column(name = "ENABLED")
    @Expose
    private Boolean enabled = true;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<StudentSubmission> submissions;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Assignment> assignments;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Token> tokens;

    public User() {

    }

    public User(String firstName, String lastName, String email, String password, Role role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
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

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<Assignment> getAssignments() {
        return assignments;
    }

    public void setAssignments(List<Assignment> assignments) {
        this.assignments = assignments;
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

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    @Override
    public String getPassword() {
        return password;
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
        return this.enabled;
    }
}
