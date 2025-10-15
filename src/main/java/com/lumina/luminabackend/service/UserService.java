package com.lumina.luminabackend.service;


import com.lumina.luminabackend.dto.auth.UserProfileDTO;
import com.lumina.luminabackend.dto.user.ChangePasswordDTO;
import com.lumina.luminabackend.dto.user.CreateAdminDTO;
import com.lumina.luminabackend.dto.user.UpdateUserProfileDTO;
import com.lumina.luminabackend.entity.user.Role;
import com.lumina.luminabackend.entity.user.User;
import com.lumina.luminabackend.exception.DuplicateResourceException;
import com.lumina.luminabackend.exception.ResourceNotFoundException;
import com.lumina.luminabackend.exception.UnauthorizedException;
import com.lumina.luminabackend.repository.user.RoleRepository;
import com.lumina.luminabackend.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Client methods
     */
    @Transactional(readOnly = true)
    public UserProfileDTO getUserProfile(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        return convertToUserProfileDTO(user);
    }

    @Transactional
    public UserProfileDTO updateUserProfile(Integer userId, UpdateUserProfileDTO updateDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        String newEmail = updateDTO.getEmail().toLowerCase();

        if (!user.getEmail().equals(newEmail) && userRepository.existsByEmail(newEmail)) {
            throw new DuplicateResourceException("El email ya está en uso");
        }

        user.setFirstName(updateDTO.getFirstName());
        user.setLastName(updateDTO.getLastName());
        user.setEmail(newEmail);
        user.setPhone(updateDTO.getPhone());

        userRepository.save(user);
        return convertToUserProfileDTO(user);
    }

    @Transactional
    public void changePassword(Integer userId, ChangePasswordDTO changePasswordDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        if (!passwordEncoder.matches(changePasswordDTO.getCurrentPassword(), user.getPassword())) {
            throw new UnauthorizedException("La contraseña actual es incorrecta");
        }

        if (!changePasswordDTO.getNewPassword().equals(changePasswordDTO.getConfirmPassword())) {
            throw new IllegalArgumentException("Las contraseñas no coinciden");
        }

        if (passwordEncoder.matches(changePasswordDTO.getNewPassword(), user.getPassword())) {
            throw new IllegalArgumentException("La nueva contraseña debe ser diferente a la actual");
        }

        user.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
        userRepository.save(user);
    }

    /**
     * Admin methods
     */
    @Transactional
    public UserProfileDTO createAdmin(CreateAdminDTO createDTO) {
        String email = createDTO.getEmail().toLowerCase();

        if (userRepository.existsByEmail(email)) {
            throw new DuplicateResourceException("El email ya está registrado");
        }

        if (userRepository.existsByDni(createDTO.getDni())) {
            throw new DuplicateResourceException("El DNI ya está registrado");
        }

        Role adminRole = roleRepository.findByRoleName("ADMIN")
                .orElseThrow(() -> new ResourceNotFoundException("Rol ADMIN no encontrado"));

        User newAdmin = User.builder()
                .firstName(createDTO.getFirstName())
                .lastName(createDTO.getLastName())
                .dni(createDTO.getDni())
                .phone(createDTO.getPhone())
                .email(email)
                .password(passwordEncoder.encode(createDTO.getPassword()))
                .role(adminRole)
                .registrationDate(LocalDateTime.now())
                .build();

        userRepository.save(newAdmin);
        return convertToUserProfileDTO(newAdmin);
    }

    @Transactional(readOnly = true)
    public List<UserProfileDTO> getAllClients() {
        return userRepository.findByRoleName("CLIENTE")
                .stream()
                .map(this::convertToUserProfileDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserProfileDTO> searchClients(String keyword) {
        return userRepository.findClientsByNameOrLastName(keyword)
                .stream()
                .map(this::convertToUserProfileDTO)
                .collect(Collectors.toList());
    }

    /**
     * Private methods
     */
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