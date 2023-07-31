package com.code.auditor.enums;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.code.auditor.enums.Permission.*;

public enum Role {
    ADMIN(
            Set.of(
                    ADMIN_READ,
                    ADMIN_UPDATE,
                    ADMIN_DELETE,
                    ADMIN_CREATE,
                    PROFESSOR_CREATE,
                    PROFESSOR_READ,
                    PROFESSOR_UPDATE,
                    PROFESSOR_DELETE
                )
    ),
    PROFESSOR(
            Set.of(
                    PROFESSOR_CREATE,
                    PROFESSOR_READ,
                    PROFESSOR_UPDATE,
                    PROFESSOR_DELETE
            )
    ),
    STUDENT(
            Set.of(
                    STUDENT_CREATE,
                    STUDENT_READ,
                    STUDENT_UPDATE,
                    STUDENT_DELETE
            )
    );

    private final Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}
