package com.lumina.luminabackend.security;

import com.lumina.luminabackend.entity.user.Role;
import com.lumina.luminabackend.entity.user.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * Adapts the User entity to Spring Security's UserDetails.
 */
public record CustomUserPrincipal(User user) implements UserDetails {

    /** Creates a CustomUserPrincipal instance from a User entity. */
    public static CustomUserPrincipal create(User user) {
        return new CustomUserPrincipal(user);
    }

    /** Returns user granted authority based on role. */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Role role = user.getRole();
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.getRoleName().toUpperCase()));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    /** Checks if the account is non-expired (true = active) */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /** Checks if the account is non-locked (true = unlocked) */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /** Checks if credentials are non-expired (true = valid) */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /** Checks if the account is enabled and allowed to log in. */
    @Override
    public boolean isEnabled() {
        return true;
    }

    public Integer getId() {
        return user.getUserId();
    }

    public String getEmail() {
        return user.getEmail();
    }

    public String getRoleName() {
        return user.getRole().getRoleName();
    }

}