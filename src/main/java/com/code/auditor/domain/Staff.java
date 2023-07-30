package com.code.auditor.domain;

import com.code.auditor.enums.Role;
import com.google.gson.annotations.Expose;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "STAFF")
public class Staff implements UserDetails {

    /**
     * The id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    /**
     * The first name of the staff.
     */
    @Column(name = "FIRST_NAME", columnDefinition = "VARCHAR", nullable = false)
    @Expose
    private String firstName;

    /**
     * The last name of the staff.
     */
    @Column(name = "LAST_NAME", columnDefinition = "VARCHAR", nullable = false)
    @Expose
    private String lastName;

    /**
     * The email of the staff.
     */
    @Column(nullable = false, unique = true)
    @Expose
    private String email;

    /**
     * The password of the staff.
     */
    @Column(nullable = false)
    private String password;

    /**
     * The title of the staff.
     */
    @Expose
    private String title;

    @Enumerated(EnumType.ORDINAL)
    @Expose
    @Column(name = "ROLE", columnDefinition = "VARCHAR", nullable = false)
    private Role role;

    @OneToMany(mappedBy = "staff", cascade = CascadeType.ALL)
    private List<Assignment> assignments;

    @OneToMany(mappedBy = "staff")
    private List<Token> tokens;

    public Staff() {

    }

    public Staff(String firstName, String lastName, String email, String password, Role role) {
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
        return true;
    }
}
