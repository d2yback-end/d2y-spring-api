package com.d2y.d2yspringapi.models;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import static com.d2y.d2yspringapi.models.Permission.ADMIN_READ;
import static com.d2y.d2yspringapi.models.Permission.ADMIN_UPDATE;
import static com.d2y.d2yspringapi.models.Permission.ADMIN_DELETE;
import static com.d2y.d2yspringapi.models.Permission.ADMIN_CREATE;
import static com.d2y.d2yspringapi.models.Permission.MANAGER_READ;
import static com.d2y.d2yspringapi.models.Permission.MANAGER_UPDATE;
import static com.d2y.d2yspringapi.models.Permission.MANAGER_DELETE;
import static com.d2y.d2yspringapi.models.Permission.MANAGER_CREATE;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Role {

    USER(Collections.emptySet()),
    ADMIN(
            Set.of(
                    ADMIN_READ,
                    ADMIN_UPDATE,
                    ADMIN_DELETE,
                    ADMIN_CREATE,
                    MANAGER_READ,
                    MANAGER_UPDATE,
                    MANAGER_DELETE,
                    MANAGER_CREATE)),
    MANAGER(
            Set.of(
                    MANAGER_READ,
                    MANAGER_UPDATE,
                    MANAGER_DELETE,
                    MANAGER_CREATE));

    @Getter
    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}
