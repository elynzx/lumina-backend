package com.lumina.luminabackend.service;

import com.lumina.luminabackend.dto.auth.*;
import com.lumina.luminabackend.entity.user.Role;
import com.lumina.luminabackend.entity.user.User;
import com.lumina.luminabackend.exception.DuplicateResourceException;
import com.lumina.luminabackend.exception.ResourceNotFoundException;
import com.lumina.luminabackend.exception.UnauthorizedException;
import com.lumina.luminabackend.repository.user.RoleRepository;
import com.lumina.luminabackend.repository.user.UserRepository;
import com.lumina.luminabackend.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public AuthResponseDTO register(RegisterRequestDTO request) {
        String email = request.getEmail().toLowerCase();

        if (userRepository.existsByEmail(email)) {
            throw new DuplicateResourceException("El email ya está registrado");
        }

        if (userRepository.existsByDni(request.getDni())) {
            throw new DuplicateResourceException("El DNI ya está registrado");
        }

        Role clientRole = roleRepository.findByRoleName("CLIENTE")
                .orElseThrow(() -> new RuntimeException("Rol CLIENTE no encontrado"));

        User newUser = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .dni(request.getDni())
                .phone(request.getPhone())
                .email(email)
                .password(passwordEncoder.encode(request.getPassword()))
                .role(clientRole)
                .registrationDate(LocalDateTime.now())
                .build();

        userRepository.save(newUser);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, request.getPassword())
        );

        String token = jwtTokenProvider.generateToken(authentication);

        return AuthResponseDTO.builder()
                .token(token)
                .tokenType("Bearer")
                .user(convertToUserProfileDTO(newUser))
                .build();
    }

    @Transactional(readOnly = true)
    public AuthResponseDTO login(LoginRequestDTO request) {
        String email = request.getEmail().toLowerCase();

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, request.getPassword())
        );

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        if (!"CLIENTE".equals(user.getRole().getRoleName())) {
            throw new UnauthorizedException("Acceso denegado. Use el login de administrador");
        }

        String token = jwtTokenProvider.generateToken(authentication);

        return AuthResponseDTO.builder()
                .token(token)
                .tokenType("Bearer")
                .user(convertToUserProfileDTO(user))
                .build();
    }

    @Transactional(readOnly = true)
    public AuthResponseDTO adminLogin(AdminLoginRequestDTO request) {
        String email = request.getEmail().toLowerCase();

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, request.getPassword())
        );

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        if (!"ADMIN".equals(user.getRole().getRoleName())) {
            throw new UnauthorizedException("No tiene permisos de administrador");
        }

        String token = jwtTokenProvider.generateToken(authentication);

        return AuthResponseDTO.builder()
                .token(token)
                .tokenType("Bearer")
                .user(convertToUserProfileDTO(user))
                .build();
    }

    private UserProfileDTO convertToUserProfileDTO(User user) {
        return UserProfileDTO.builder()
                .userId(user.getUserId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .roleName(user.getRole().getRoleName())
                .build();
    }
}