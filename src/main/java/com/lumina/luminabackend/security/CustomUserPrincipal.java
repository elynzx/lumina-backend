package com.lumina.luminabackend.security;

import com.lumina.luminabackend.entity.Role;
import com.lumina.luminabackend.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserPrincipal implements UserDetails {

    private final User user; // reference to User entity

    public CustomUserPrincipal(User user) {
        this.user = user;
    }

    public static CustomUserPrincipal create(User user) {
        return new CustomUserPrincipal(user);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Role role = user.getRole();
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.getName().toUpperCase())); // user roles
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // always active
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // not locked
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // credentials valid
    }

    @Override
    public boolean isEnabled() {
        return true; // account enabled
    }

    public User getUser() {
        return user;
    }
}