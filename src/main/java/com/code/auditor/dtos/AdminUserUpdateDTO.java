package com.code.auditor.dtos;

import com.code.auditor.enums.Role;

import java.security.InvalidParameterException;

public class AdminUserUpdateDTO {
    private String firstName;
    private String lastName;
    private String role;
    private String title;
    private String faculty;
    private String facultyNumber;
    private String specialization;
    private String group;
    private String stream;

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

    public Role getRole() {
        try {
            return Role.valueOf(this.role.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidParameterException("Невалидна роля");
        }
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
}
